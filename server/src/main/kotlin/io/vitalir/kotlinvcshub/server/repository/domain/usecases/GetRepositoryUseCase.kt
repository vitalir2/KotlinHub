package io.vitalir.kotlinvcshub.server.repository.domain.usecases

import arrow.core.Either
import io.vitalir.kotlinvcshub.server.repository.domain.Repository
import io.vitalir.kotlinvcshub.server.repository.domain.RepositoryError
import io.vitalir.kotlinvcshub.server.user.domain.model.UserId

interface GetRepositoryUseCase {

    suspend fun invoke(userId: UserId, name: String): Either<RepositoryError.Get, Repository>
}
