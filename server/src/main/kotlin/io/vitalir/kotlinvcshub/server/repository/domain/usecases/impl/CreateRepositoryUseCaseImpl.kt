package io.vitalir.kotlinvcshub.server.repository.domain.usecases.impl

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.vitalir.kotlinvcshub.server.common.domain.LocalDateTimeProvider
import io.vitalir.kotlinvcshub.server.repository.domain.CreateRepositoryData
import io.vitalir.kotlinvcshub.server.repository.domain.Repository
import io.vitalir.kotlinvcshub.server.repository.domain.RepositoryError
import io.vitalir.kotlinvcshub.server.repository.domain.RepositoryPersistence
import io.vitalir.kotlinvcshub.server.repository.domain.usecases.CreateRepositoryUseCase
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
        val createdAt = localDateTimeProvider.now()
        val fakeRepository = Repository(
            ownerId = initData.userId,
            name = initData.name,
            accessMode = initData.accessMode,
            description = initData.description,
            createdAt = createdAt,
            lastUpdated = createdAt,
        )
        repositoryPersistence.addRepository(fakeRepository)
    }
}
