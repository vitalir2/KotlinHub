package io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase

import arrow.core.Either
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.CreateRepositoryData
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryError
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.shared.common.network.Url
import io.vitalir.kotlinhub.shared.feature.user.UserId

interface CreateRepositoryUseCase {

    suspend operator fun invoke(initData: CreateRepositoryData): CreateRepositoryResult

    sealed interface Error {
        object Unknown : Error, RepositoryError.Unknown

        data class UserDoesNotExist(
            override val userIdentifier: UserIdentifier,
        ) : Error, RepositoryError.UserDoesNotExist

        data class RepositoryAlreadyExists(
            val userId: UserId,
            val repositoryName: String,
        ) : Error
    }
}

typealias CreateRepositoryResult = Either<CreateRepositoryUseCase.Error, Url>
