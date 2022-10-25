package io.vitalir.kotlinvcshub.server.user.domain.usecase.impl

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvcshub.server.user.domain.model.UserCredentials
import io.vitalir.kotlinvcshub.server.user.domain.model.UserError
import io.vitalir.kotlinvcshub.server.user.domain.persistence.UserPersistence
import io.vitalir.kotlinvcshub.server.user.domain.usecase.RegisterUserUseCase
import io.vitalir.kotlinvcshub.server.user.domain.validation.UserValidationRule

internal class RegisterUserUseCaseImpl(
    private val identifierValidationRule: UserValidationRule<UserCredentials.Identifier>,
    private val userPersistence: UserPersistence,
) : RegisterUserUseCase {

    override suspend fun invoke(credentials: UserCredentials): Either<UserError, User> = either {
        validateCredentials(credentials).bind()
        checkIfUserExists(credentials.identifier).bind()
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
        return identifierValidationRule.validate(credentials.identifier)
    }

    private suspend fun checkIfUserExists(
        identifier: UserCredentials.Identifier,
    ): Either<UserError.UserAlreadyExists, Unit> {
        val userOrNull = userPersistence.getUser(identifier).orNull()
        return if (userOrNull == null) {
            Unit.right()
        } else {
            UserError.UserAlreadyExists.left()
        }
    }
}
