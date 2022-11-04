package io.vitalir.kotlinhub.server.app.repository.domain.usecase.impl

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.vitalir.kotlinhub.server.app.common.domain.LocalDateTimeProvider
import io.vitalir.kotlinhub.server.app.common.domain.Uri
import io.vitalir.kotlinhub.server.app.infrastructure.git.GitManager
import io.vitalir.kotlinhub.server.app.repository.domain.model.CreateRepositoryData
import io.vitalir.kotlinhub.server.app.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.repository.domain.model.RepositoryError
import io.vitalir.kotlinhub.server.app.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinhub.server.app.repository.domain.usecase.CreateRepositoryUseCase
import io.vitalir.kotlinhub.server.app.user.domain.persistence.UserPersistence

internal class CreateRepositoryUseCaseImpl(
    private val userPersistence: UserPersistence,
    private val repositoryPersistence: RepositoryPersistence,
    private val localDateTimeProvider: LocalDateTimeProvider,
    private val gitManager: GitManager,
) : CreateRepositoryUseCase {

    override suspend fun invoke(initData: CreateRepositoryData): Either<RepositoryError.Create, Uri> {
        return when {
            userPersistence.isUserExists(initData.ownerId).not() ->
                RepositoryError.Create.InvalidUserId.left()
            repositoryPersistence.isRepositoryExists(initData.ownerId, initData.name) ->
                RepositoryError.Create.RepositoryAlreadyExists.left()
            else -> createRepositoryAfterValidation(initData).right()
        }
    }

    private suspend fun createRepositoryAfterValidation(initData: CreateRepositoryData): Uri {
        val repository = Repository.fromInitData(
            owner = userPersistence.getUser(initData.ownerId)!!,
            initData = initData,
            localDateTimeProvider = localDateTimeProvider,
        )
        repositoryPersistence.addRepository(repository)
        gitManager.initRepository(repository)
        return Uri.create(Uri.Scheme.GIT, repository.owner.login, "${repository.name}.git")
    }
}
