package io.vitalir.kotlinhub.server.app.user.domain.usecase

import arrow.core.Either
import io.vitalir.kotlinhub.server.app.user.domain.model.User
import io.vitalir.kotlinhub.server.app.user.domain.model.UserCredentials
import io.vitalir.kotlinhub.server.app.user.domain.model.UserError

interface LoginUseCase {

    suspend operator fun invoke(credentials: UserCredentials): Either<UserError, User>
}
