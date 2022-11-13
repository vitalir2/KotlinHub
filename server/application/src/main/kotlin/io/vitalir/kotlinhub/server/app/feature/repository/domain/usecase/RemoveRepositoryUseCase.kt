package io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase

import arrow.core.Either
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryError
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserId
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier

interface RemoveRepositoryUseCase {

    suspend operator fun invoke(
        userId: UserId,
        repositoryName: String,
    ): Either<Error, Unit>

    sealed interface Error {
        data class UserDoesNotExist(
            override val userIdentifier: UserIdentifier,
        ) : Error, RepositoryError.Common.UserDoesNotExist

        data class RepositoryDoesNotExist(
            override val userIdentifier: UserIdentifier,
            override val repositoryName: String,
        ) : Error, RepositoryError.Common.RepositoryDoesNotExist
    }
}
