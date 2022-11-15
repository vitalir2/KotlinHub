package io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.vitalir.kotlinhub.server.app.common.domain.LocalDateTimeProvider
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.CreateRepositoryData
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.CreateRepositoryResult
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.CreateRepositoryUseCase
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence
import io.vitalir.kotlinhub.server.app.infrastructure.git.GitManager
import io.vitalir.kotlinhub.shared.common.network.ServicesInfo

internal class CreateRepositoryUseCaseImpl(
    private val userPersistence: UserPersistence,
    private val repositoryPersistence: RepositoryPersistence,
    private val localDateTimeProvider: LocalDateTimeProvider,
    private val gitManager: GitManager,
) : CreateRepositoryUseCase {

    override suspend fun invoke(initData: CreateRepositoryData): CreateRepositoryResult {
        return when {
            userPersistence.isUserExists(UserIdentifier.Id(initData.ownerId)).not() -> {
                CreateRepositoryUseCase.Error.UserDoesNotExist(UserIdentifier.Id(initData.ownerId)).left()
            }
            repositoryPersistence.isRepositoryExists(initData.ownerId, initData.name) -> {
                CreateRepositoryUseCase.Error.RepositoryAlreadyExists(initData.ownerId, initData.name).left()
            }
            else -> {
                createRepositoryAfterPersistenceValidation(initData)
            }
        }
    }

    private suspend fun createRepositoryAfterPersistenceValidation(
        initData: CreateRepositoryData
    ): CreateRepositoryResult {
        val repository = Repository.fromInitData(
            // TODO remove double bang; it can cause crashes in multi-thread env
            owner = userPersistence.getUser(UserIdentifier.Id(initData.ownerId))!!,
            initData = initData,
            localDateTimeProvider = localDateTimeProvider,
        )
        val realRepositoryId = repositoryPersistence.addRepository(repository)
        val repositoryWithRealId = repository.copy(id = realRepositoryId)
        return when (val result = gitManager.initRepository(repositoryWithRealId)) {
            is Either.Left -> {
                repositoryPersistence.removeRepositoryById(realRepositoryId)
                result.value.toCreateError().left()
            }
            is Either.Right -> {
                repositoryWithRealId.createResourceUrl(
                    baseUrl = ServicesInfo.ReverseProxy.mainUrl,
                ).right()
            }
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
