package io.vitalir.kotlinvcshub.server.repository.domain.usecases

import arrow.core.Either
import io.vitalir.kotlinvcshub.server.repository.domain.CreateRepositoryData
import io.vitalir.kotlinvcshub.server.repository.domain.RepositoryError

interface CreateRepositoryUseCase {

    suspend fun invoke(initData: CreateRepositoryData): Either<RepositoryError.Create, Unit>
}
