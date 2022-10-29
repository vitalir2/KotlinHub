package io.vitalir.kotlinvcshub.server.repository.domain.usecase

import arrow.core.Either
import io.vitalir.kotlinvcshub.server.repository.domain.model.Repository
import io.vitalir.kotlinvcshub.server.repository.domain.model.RepositoryError
import io.vitalir.kotlinvcshub.server.user.domain.model.UserId

interface GetRepositoryUseCase {

    suspend fun invoke(userId: UserId, name: String): Either<RepositoryError.Get, Repository>
}
