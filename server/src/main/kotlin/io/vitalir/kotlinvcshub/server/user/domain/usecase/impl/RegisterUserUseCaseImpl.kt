package io.vitalir.kotlinvcshub.server.user.domain.usecase.impl

import arrow.core.Either
import arrow.core.left
import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvcshub.server.user.domain.model.UserCredentials
import io.vitalir.kotlinvcshub.server.user.domain.model.UserError
import io.vitalir.kotlinvcshub.server.user.domain.usecase.RegisterUserUseCase

internal class RegisterUserUseCaseImpl : RegisterUserUseCase {

    override fun invoke(credentials: UserCredentials): Either<UserError, User> {
        return UserError.InvalidCredentials.left()
    }
}
