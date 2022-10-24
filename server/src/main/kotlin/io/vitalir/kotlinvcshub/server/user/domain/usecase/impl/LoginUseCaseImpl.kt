package io.vitalir.kotlinvcshub.server.user.domain.usecase.impl

import arrow.core.Either
import arrow.core.continuations.either
import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvcshub.server.user.domain.model.UserError
import io.vitalir.kotlinvcshub.server.user.domain.persistence.UserPersistence
import io.vitalir.kotlinvcshub.server.user.domain.usecase.LoginUseCase
import io.vitalir.kotlinvcshub.server.user.domain.validation.EmailValidationRule
import io.vitalir.kotlinvcshub.server.user.domain.validation.LoginValidationRule

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
            is User.Credentials.Identifier.Email -> EmailValidationRule.validate(identifier)
            is User.Credentials.Identifier.Login -> LoginValidationRule.validate(identifier)
        }
    }

    // TODO
    private fun passwordIsCorrect(
        password: String,
        hashedUserPassword: String
    ): Either<UserError.InvalidCredentials, Unit> =
        Either.Right(Unit)
}
