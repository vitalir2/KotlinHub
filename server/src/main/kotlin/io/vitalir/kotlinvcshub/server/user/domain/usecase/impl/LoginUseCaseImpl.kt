package io.vitalir.kotlinvcshub.server.user.domain.usecase.impl

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import io.vitalir.kotlinvcshub.server.user.domain.usecase.LoginUseCase
import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvcshub.server.user.domain.model.UserError
import io.vitalir.kotlinvcshub.server.user.domain.persistence.UserPersistence

internal class LoginUseCaseImpl(
    private val userPersistence: UserPersistence,
) : LoginUseCase {

    override suspend fun invoke(credentials: User.Credentials): Either<UserError, User> = either {
        validateCredentials(credentials).bind()
        val user = userPersistence.getUser(credentials.identifier).bind()
        passwordIsCorrect(credentials.password, user.password).bind()
        user
    }

    private suspend fun validateCredentials(credentials: User.Credentials): Either<UserError.ValidationFailed, Unit> =
        either {
            validateIdentifier(credentials.identifier).bind()
        }

    private fun validateIdentifier(identifier: User.Credentials.Identifier): Either<UserError.ValidationFailed, Unit> {
        return when (identifier) {
            is User.Credentials.Identifier.Email -> validateEmail(identifier)
            is User.Credentials.Identifier.Login -> validateLogin(identifier)
        }
    }

    private fun validateEmail(email: User.Credentials.Identifier.Email): Either<UserError.ValidationFailed, Unit> {
        val emailRegex = EMAIL_REGEX
        return if (emailRegex.matches(email.value)) {
            Unit.right()
        } else {
            UserError.ValidationFailed.left()
        }
    }

    private fun validateLogin(login: User.Credentials.Identifier.Login): Either<UserError.ValidationFailed, Unit> {
        return if (login.value.length in LOGIN_LENGTH_RANGE) {
            Unit.right()
        } else {
            UserError.ValidationFailed.left()
        }
    }

    // TODO
    private fun passwordIsCorrect(
        password: String,
        hashedUserPassword: String
    ): Either<UserError.InvalidCredentials, Unit> =
        Either.Right(Unit)

    companion object {
        private const val LOGIN_MIN_LENGTH = 6
        private const val LOGIN_MAX_LENGTH = 20

        // TODO: Add real regex
        private val EMAIL_REGEX = Regex("TODO")
        private val LOGIN_LENGTH_RANGE = LOGIN_MIN_LENGTH..LOGIN_MAX_LENGTH
    }
}
