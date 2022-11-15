package io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase

import arrow.core.Either
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryError
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier

interface UpdateRepositoryUseCase {

    suspend operator fun invoke(
        userIdentifier: UserIdentifier,
        repositoryName: String,
        updateRepositoryData: UpdateRepositoryData,
    ): UpdateRepositoryResult

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

typealias UpdateRepositoryResult = Either<UpdateRepositoryUseCase.Error, Unit>
