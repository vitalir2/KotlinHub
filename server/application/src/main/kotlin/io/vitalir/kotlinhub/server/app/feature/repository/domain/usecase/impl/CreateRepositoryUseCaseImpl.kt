package io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl

import arrow.core.left
import arrow.core.right
import io.vitalir.kotlinhub.server.app.common.domain.LocalDateTimeProvider
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.CreateRepositoryData
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryError
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.CreateRepositoryResult
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.CreateRepositoryUseCase
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence
import io.vitalir.kotlinhub.server.app.infrastructure.git.GitManager
import io.vitalir.kotlinhub.shared.common.network.Url

internal class CreateRepositoryUseCaseImpl(
    private val userPersistence: UserPersistence,
    private val repositoryPersistence: RepositoryPersistence,
    private val localDateTimeProvider: LocalDateTimeProvider,
    private val gitManager: GitManager,
) : CreateRepositoryUseCase {

    override suspend fun invoke(initData: CreateRepositoryData): CreateRepositoryResult {
        return when {
            userPersistence.isUserExists(UserIdentifier.Id(initData.ownerId)).not() ->
                RepositoryError.Create.InvalidUserId(initData.ownerId).left()
            repositoryPersistence.isRepositoryExists(initData.ownerId, initData.name) ->
                RepositoryError.Create.RepositoryAlreadyExists(initData.ownerId, initData.name).left()
            else -> createRepositoryAfterValidation(initData).right()
        }
    }

    private suspend fun createRepositoryAfterValidation(initData: CreateRepositoryData): Url {
        val repository = Repository.fromInitData(
            owner = userPersistence.getUser(UserIdentifier.Id(initData.ownerId))!!,
            initData = initData,
            localDateTimeProvider = localDateTimeProvider,
        )
        repositoryPersistence.addRepository(repository)
        gitManager.initRepository(repository)
        return repository.createResourceUrl()
    }
}
