package io.vitalir.kotlinhub.server.app.user.domain.usecase.impl

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import io.vitalir.kotlinhub.server.app.user.domain.model.User
import io.vitalir.kotlinhub.server.app.user.domain.model.UserCredentials
import io.vitalir.kotlinhub.server.app.user.domain.model.UserError
import io.vitalir.kotlinhub.server.app.user.domain.password.PasswordManager
import io.vitalir.kotlinhub.server.app.user.domain.persistence.UserPersistence
import io.vitalir.kotlinhub.server.app.user.domain.usecase.RegisterUserUseCase
import io.vitalir.kotlinhub.server.app.user.domain.validation.UserValidationRule

internal class RegisterUserUseCaseImpl(
    private val identifierValidationRule: UserValidationRule<UserCredentials.Identifier>,
    private val userPersistence: UserPersistence,
    private val passwordManager: PasswordManager,
) : RegisterUserUseCase {

    override suspend fun invoke(credentials: UserCredentials): Either<UserError, User> = either {
        validateCredentials(credentials).bind()
        checkIfUserExists(credentials.identifier).bind()
        val newUser = User.fromCredentials(credentials, passwordManager)
        userPersistence.addUser(newUser)
        newUser
    }

    private fun validateCredentials(credentials: UserCredentials): Either<UserError.ValidationFailed, Unit> {
        return identifierValidationRule.validate(credentials.identifier)
    }

    private suspend fun checkIfUserExists(
        identifier: UserCredentials.Identifier,
    ): Either<UserError.UserAlreadyExists, Unit> {
        return if (userPersistence.isUserExists(identifier).not()) {
            Unit.right()
        } else {
            UserError.UserAlreadyExists.left()
        }
    }
}
