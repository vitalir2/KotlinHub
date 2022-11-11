package io.vitalir.kotlinhub.server.app.feature.user.domain.usecase

import arrow.core.Either
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserError
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserId

interface UpdateUserUseCase {

    suspend operator fun invoke(
        userId: UserId,
        username: String?,
        email: String?,
    ): Either<UserError, Unit>
}
