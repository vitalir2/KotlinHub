package io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.impl

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserCredentials
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserError
import io.vitalir.kotlinhub.server.app.infrastructure.auth.PasswordManager
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.LoginUseCase
import io.vitalir.kotlinhub.server.app.feature.user.domain.validation.IdentifierValidationRule

internal class LoginUseCaseImpl(
    private val userPersistence: UserPersistence,
    private val passwordManager: PasswordManager,
) : LoginUseCase {

    override suspend fun invoke(credentials: UserCredentials): Either<UserError, User> = either {
        validateCredentials(credentials).bind()
        val user = userPersistence.getUser(credentials.identifier).bind()
        passwordIsCorrectForUser(credentials.password, user.password).bind()
        user
    }

    private suspend fun validateCredentials(credentials: UserCredentials): Either<UserError.ValidationFailed, Unit> =
        either {
            validateIdentifier(credentials.identifier).bind()
        }

    private fun validateIdentifier(identifier: UserCredentials.Identifier): Either<UserError.ValidationFailed, Unit> {
        return IdentifierValidationRule.validate(identifier)
    }
    private fun passwordIsCorrectForUser(
        outerPassword: String,
        userHashedPassword: String
    ): Either<UserError.InvalidCredentials, Unit> {
        return if (passwordManager.comparePasswords(outerPassword, userHashedPassword)) {
            Unit.right()
        } else {
            UserError.InvalidCredentials.left()
        }
    }
}
