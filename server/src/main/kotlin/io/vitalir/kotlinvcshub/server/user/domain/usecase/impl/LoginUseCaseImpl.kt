package io.vitalir.kotlinvcshub.server.user.domain.usecase.impl

import arrow.core.Either
import arrow.core.continuations.either
import io.vitalir.kotlinvcshub.server.user.domain.usecase.LoginUseCase
import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvcshub.server.user.domain.model.UserError
import io.vitalir.kotlinvcshub.server.user.domain.persistence.UserPersistence

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
