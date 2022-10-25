package io.vitalir.kotlinvcshub.server.user.domain.usecase.impl

import arrow.core.Either
import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvcshub.server.user.domain.model.UserError
import io.vitalir.kotlinvcshub.server.user.domain.usecase.RegisterUserUseCase

internal class RegisterUserUseCaseImpl : RegisterUserUseCase {

    override fun invoke(credentials: User.Credentials): Either<UserError, User> {
        TODO("Not yet implemented")
    }
}
