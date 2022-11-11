package io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase

import arrow.core.Either
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryError

typealias GetRepositoryResult = Either<RepositoryError.Get, Repository>

interface GetRepositoryUseCase {

    suspend operator fun invoke(
        username: String,
        repositoryName: String,
    ): GetRepositoryResult
}
