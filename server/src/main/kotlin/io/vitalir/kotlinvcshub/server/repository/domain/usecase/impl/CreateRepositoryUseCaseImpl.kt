package io.vitalir.kotlinvcshub.server.repository.domain.usecase.impl

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.vitalir.kotlinvcshub.server.common.domain.LocalDateTimeProvider
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
) : CreateRepositoryUseCase {

    override suspend fun invoke(initData: CreateRepositoryData): Either<RepositoryError.Create, Unit> {
        return when {
            userPersistence.isUserExists(initData.userId).not() ->
                RepositoryError.Create.InvalidUserId.left()
            repositoryPersistence.isRepositoryExists(initData.userId, initData.name) ->
                RepositoryError.Create.RepositoryAlreadyExists.left()
            else -> createRepositoryAfterValidation(initData).right()
        }
    }

    private suspend fun createRepositoryAfterValidation(initData: CreateRepositoryData) {
        repositoryPersistence.addRepository(Repository.fromInitData(initData, localDateTimeProvider))
    }
}
