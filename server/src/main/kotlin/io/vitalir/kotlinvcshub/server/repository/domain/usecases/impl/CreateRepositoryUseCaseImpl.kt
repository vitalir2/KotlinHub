package io.vitalir.kotlinvcshub.server.repository.domain.usecases.impl

import arrow.core.Either
import arrow.core.right
import io.vitalir.kotlinvcshub.server.repository.domain.CreateRepositoryData
import io.vitalir.kotlinvcshub.server.repository.domain.RepositoryError
import io.vitalir.kotlinvcshub.server.repository.domain.usecases.CreateRepositoryUseCase

internal class CreateRepositoryUseCaseImpl : CreateRepositoryUseCase {

    override suspend fun invoke(initData: CreateRepositoryData): Either<RepositoryError.Create, Unit> {
        return Unit.right()
    }
}
