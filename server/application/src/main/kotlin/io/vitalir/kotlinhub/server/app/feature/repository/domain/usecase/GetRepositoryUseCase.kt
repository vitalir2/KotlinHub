package io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase

import arrow.core.Either
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryError
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.shared.feature.user.UserId

interface GetRepositoryUseCase {

    suspend operator fun invoke(
        userIdentifier: UserIdentifier,
        repositoryName: String,
        currentUserId: UserId?,
    ): GetRepositoryResult

    suspend operator fun invoke(
        repositoryIdentifier: RepositoryIdentifier
    ): Either<io.vitalir.kotlinhub.server.app.feature.repository.domain.model.error.RepositoryError, Repository>

    sealed interface Error {

        data class UserDoesNotExist(
            override val userIdentifier: UserIdentifier,
        ) : Error, RepositoryError.UserDoesNotExist

        data class RepositoryDoesNotExist(
            override val userIdentifier: UserIdentifier,
            override val repositoryName: String,
        ) : Error, RepositoryError.RepositoryDoesNotExist
    }
}

typealias GetRepositoryResult = Either<GetRepositoryUseCase.Error, Repository>
