package io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.impl

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserCredentials
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserError
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.infrastructure.auth.PasswordManager
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.RegisterUserUseCase
import io.vitalir.kotlinhub.server.app.feature.user.domain.validation.UserValidationRule

internal class RegisterUserUseCaseImpl(
    private val identifierValidationRule: UserValidationRule<UserIdentifier>,
    private val userPersistence: UserPersistence,
    private val passwordManager: PasswordManager,
) : RegisterUserUseCase {

    override suspend fun invoke(credentials: UserCredentials): Either<UserError, User> = either {
        validateCredentials(credentials).bind()
        checkIfUserExists(credentials.identifier).bind()
        val newUser = User.fromCredentials(credentials, passwordManager)
        userPersistence.addUser(newUser).bind()
        newUser
    }

    private fun validateCredentials(credentials: UserCredentials): Either<UserError.ValidationFailed, Unit> {
        return identifierValidationRule.validate(credentials.identifier)
    }

    private suspend fun checkIfUserExists(
        identifier: UserIdentifier,
    ): Either<UserError.UserAlreadyExists, Unit> {
        return if (userPersistence.isUserExists(identifier).not()) {
            Unit.right()
        } else {
            UserError.UserAlreadyExists.left()
        }
    }
}
