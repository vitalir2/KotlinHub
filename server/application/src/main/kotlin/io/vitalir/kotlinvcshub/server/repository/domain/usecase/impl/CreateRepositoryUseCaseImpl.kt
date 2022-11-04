package io.vitalir.kotlinvcshub.server.repository.domain.usecase.impl

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.vitalir.kotlinvcshub.server.common.domain.LocalDateTimeProvider
import io.vitalir.kotlinvcshub.server.common.domain.Uri
import io.vitalir.kotlinvcshub.server.infrastructure.git.GitManager
import io.vitalir.kotlinvcshub.server.repository.domain.model.CreateRepositoryData
import io.vitalir.kotlinvcshub.server.repository.domain.model.Repository
import io.vitalir.kotlinvcshub.server.repository.domain.model.RepositoryError
import io.vitalir.kotlinvcshub.server.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinvcshub.server.repository.domain.usecase.CreateRepositoryUseCase
import io.vitalir.kotlinvcshub.server.user.domain.persistence.UserPersistence

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
