package io.vitalir.kotlinvcshub.server.user.domain.impl

import arrow.core.Either
import arrow.core.continuations.either
import io.vitalir.kotlinvcshub.server.user.domain.LoginUseCase
import io.vitalir.kotlinvcshub.server.user.domain.User
import io.vitalir.kotlinvcshub.server.user.domain.UserError
import io.vitalir.kotlinvcshub.server.user.domain.UserPersistence

internal class LoginUseCaseImpl(
    private val userPersistence: UserPersistence,
) : LoginUseCase {

    override suspend fun invoke(credentials: User.Credentials): Either<UserError, User> = either {
        val validatedCredentials = credentials.validate().bind()
        val user = userPersistence.getUser(validatedCredentials.identifier).bind()
        passwordIsCorrect(validatedCredentials.password, user.password).bind()
        user
    }

    // TODO
    private fun User.Credentials.validate(): Either<UserError.InvalidCredentialsFormat, User.Credentials> =
        Either.Right(this)

    // TODO
    private fun passwordIsCorrect(password: String, hashedUserPassword: String): Either<UserError.InvalidCredentials, Unit> =
        Either.Right(Unit)
}
