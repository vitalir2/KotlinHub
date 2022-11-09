package io.vitalir.kotlinhub.server.app.repository.domain.usecase

import arrow.core.Either
import io.vitalir.kotlinhub.server.app.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.repository.domain.model.RepositoryError

interface GetRepositoryUseCase {

    suspend operator fun invoke(
        userName: String,
        repositoryName: String,
    ): Either<RepositoryError.Get, Repository>
}
