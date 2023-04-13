package io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import arrow.core.rightIfNotNull
import arrow.core.rightIfNull
import io.vitalir.kotlinhub.server.app.common.domain.LocalDateTimeProvider
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.CreateRepositoryData
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.CreateRepositoryResult
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.CreateRepositoryUseCase
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence
import io.vitalir.kotlinhub.server.app.infrastructure.git.GitManager

internal class CreateRepositoryUseCaseImpl(
    private val userPersistence: UserPersistence,
    private val repositoryPersistence: RepositoryPersistence,
    private val localDateTimeProvider: LocalDateTimeProvider,
    private val gitManager: GitManager,
) : CreateRepositoryUseCase {

    override suspend fun invoke(initData: CreateRepositoryData): CreateRepositoryResult {
        return either {
            val userIdentifier = UserIdentifier.Id(initData.ownerId)
            val user = userPersistence.getUser(userIdentifier).rightIfNotNull {
                CreateRepositoryUseCase.Error.UserDoesNotExist(userIdentifier)
            }.bind()
            repositoryPersistence.getRepository(userIdentifier, initData.name).rightIfNull {
                CreateRepositoryUseCase.Error.RepositoryAlreadyExists(initData.ownerId, initData.name)
            }.bind()
            createRepositoryAfterPersistenceValidation(user, initData).bind()
        }
    }

    private suspend fun createRepositoryAfterPersistenceValidation(
        owner: User,
        initData: CreateRepositoryData,
    ): CreateRepositoryResult {
        val repository = Repository.fromInitData(
            owner = owner,
            initData = initData,
            localDateTimeProvider = localDateTimeProvider,
        )
        val realRepositoryId = repositoryPersistence.addRepository(repository)
        val repositoryWithRealId = repository.copy(id = realRepositoryId)
        return when (val result = gitManager.initRepository(repositoryWithRealId)) {
            is Either.Left -> {
                repositoryPersistence.removeRepositoryById(realRepositoryId) // TODO think about transactions
                result.value.toCreateError().left()
            }

            is Either.Right -> repositoryWithRealId.id.right()
        }
    }

    companion object {
        private fun GitManager.Error.toCreateError(): CreateRepositoryUseCase.Error {
            return when (this) {
                is GitManager.Error.RepositoryAlreadyExists -> {
                    CreateRepositoryUseCase.Error.RepositoryAlreadyExists(
                        userId = repository.owner.id,
                        repositoryName = repository.name,
                    )
                }

                is GitManager.Error.Unknown -> {
                    CreateRepositoryUseCase.Error.Unknown
                }
            }
        }
    }
}
