package io.vitalir.kotlinvcshub.server.user.domain.usecase.impl

import arrow.core.Either
import arrow.core.continuations.either
import io.vitalir.kotlinvcshub.server.user.domain.usecase.LoginUseCase
import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvcshub.server.user.domain.model.UserError
import io.vitalir.kotlinvcshub.server.user.domain.persistence.UserPersistence
import io.vitalir.kotlinvcshub.server.user.domain.usecase.ValidateUserCredentialsUseCase

internal class LoginUseCaseImpl(
    private val userPersistence: UserPersistence,
    private val validateUserCredentialsUseCase: ValidateUserCredentialsUseCase,
) : LoginUseCase {

    override suspend fun invoke(credentials: User.Credentials): Either<UserError, User> = either {
        validateUserCredentialsUseCase(credentials).bind()
        val user = userPersistence.getUser(credentials.identifier).bind()
        passwordIsCorrect(credentials.password, user.password).bind()
        user
    }

    // TODO
    private fun passwordIsCorrect(password: String, hashedUserPassword: String): Either<UserError.InvalidCredentials, Unit> =
        Either.Right(Unit)
}
