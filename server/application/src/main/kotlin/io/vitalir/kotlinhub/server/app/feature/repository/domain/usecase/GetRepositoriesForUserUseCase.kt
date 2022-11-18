package io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase

import arrow.core.Either
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryError
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier

interface GetRepositoriesForUserUseCase {

    suspend operator fun invoke(userIdentifier: UserIdentifier): Either<Error, List<Repository>>

    sealed interface Error {
        data class UserDoesNotExist(
            override val userIdentifier: UserIdentifier,
        ) : Error, RepositoryError.UserDoesNotExist
    }
}
