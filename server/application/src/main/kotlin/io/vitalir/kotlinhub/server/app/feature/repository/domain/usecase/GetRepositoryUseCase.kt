package io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase

import arrow.core.Either
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryError
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.shared.feature.user.UserId

interface GetRepositoryUseCase {

    suspend operator fun invoke(
        userIdentifier: UserIdentifier,
        repositoryName: String,
    ): GetRepositoryResult
}

typealias GetRepositoryResult = Either<RepositoryError.Get, Repository>
