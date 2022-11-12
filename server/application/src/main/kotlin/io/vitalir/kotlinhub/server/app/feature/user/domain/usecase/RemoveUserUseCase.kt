package io.vitalir.kotlinhub.server.app.feature.user.domain.usecase

import arrow.core.Either
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserId

interface RemoveUserUseCase {

    suspend operator fun invoke(userId: UserId): RemoveUserResult

    sealed interface Error {

        data class UserDoesNotExist(val userId: UserId) : Error

        object RemoveFailed : Error
    }
}

typealias RemoveUserResult = Either<RemoveUserUseCase.Error, Unit>
