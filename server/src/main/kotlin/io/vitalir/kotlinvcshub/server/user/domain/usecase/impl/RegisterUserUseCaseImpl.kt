package io.vitalir.kotlinvcshub.server.user.domain.usecase.impl

import arrow.core.Either
import arrow.core.continuations.either
import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvcshub.server.user.domain.model.UserCredentials
import io.vitalir.kotlinvcshub.server.user.domain.model.UserError
import io.vitalir.kotlinvcshub.server.user.domain.usecase.RegisterUserUseCase
import io.vitalir.kotlinvcshub.server.user.domain.validation.EmailValidationRule

internal class RegisterUserUseCaseImpl : RegisterUserUseCase {

    override suspend fun invoke(credentials: UserCredentials): Either<UserError, User> = either {
        validateCredentials(credentials).bind()
        val (login, email) = when (credentials.identifier) {
            is UserCredentials.Identifier.Email -> credentials.identifier.value to credentials.identifier.value
            is UserCredentials.Identifier.Login -> credentials.identifier.value to null
        }
        User(
            id = 123,
            login = login,
            password = credentials.password,
            email = email,
        )
    }

    private fun validateCredentials(credentials: UserCredentials): Either<UserError.ValidationFailed, Unit> {
        return when (credentials.identifier) {
            is UserCredentials.Identifier.Email -> EmailValidationRule.validate(credentials.identifier)
            is UserCredentials.Identifier.Login -> TODO()
        }
    }
}
