package io.vitalir.kotlinhub.server.app.feature.user.domain.usecase

import arrow.core.Either
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserId

interface UpdateUserUseCase {

    suspend operator fun invoke(
        userId: UserId,
        username: String? = null,
        email: String? = null,
    ): UpdateUserResult

    sealed interface Error {

        data class NoUser(val userId: UserId) : Error
        data class InvalidArguments(val message: String) : Error
    }
}

typealias UpdateUserResult = Either<UpdateUserUseCase.Error, Unit>
