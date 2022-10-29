package io.vitalir.kotlinvcshub.server.repository.domain.usecases.impl

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.vitalir.kotlinvcshub.server.repository.domain.CreateRepositoryData
import io.vitalir.kotlinvcshub.server.repository.domain.RepositoryError
import io.vitalir.kotlinvcshub.server.repository.domain.RepositoryPersistence
import io.vitalir.kotlinvcshub.server.repository.domain.usecases.CreateRepositoryUseCase
import io.vitalir.kotlinvcshub.server.user.domain.persistence.UserPersistence

internal class CreateRepositoryUseCaseImpl(
    private val userPersistence: UserPersistence,
    private val repositoryPersistence: RepositoryPersistence,
) : CreateRepositoryUseCase {

    override suspend fun invoke(initData: CreateRepositoryData): Either<RepositoryError.Create, Unit> {
        return when {
            userPersistence.isUserExists(initData.userId).not() ->
                RepositoryError.Create.InvalidUserId.left()
            repositoryPersistence.isRepositoryExists(initData.userId, initData.name) ->
                RepositoryError.Create.RepositoryAlreadyExists.left()
            else -> Unit.right()
        }
    }
}
