package io.vitalir.kotlinvcshub.server.user.domain.usecase

import arrow.core.Either
import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvcshub.server.user.domain.model.UserError

interface RegisterUserUseCase {

    operator fun invoke(credentials: User.Credentials): Either<UserError, User>
}
