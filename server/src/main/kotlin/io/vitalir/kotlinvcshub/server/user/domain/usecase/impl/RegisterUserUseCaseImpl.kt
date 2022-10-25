package io.vitalir.kotlinvcshub.server.user.domain.usecase.impl

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvcshub.server.user.domain.model.UserCredentials
import io.vitalir.kotlinvcshub.server.user.domain.model.UserError
import io.vitalir.kotlinvcshub.server.user.domain.password.PasswordManager
import io.vitalir.kotlinvcshub.server.user.domain.persistence.UserPersistence
import io.vitalir.kotlinvcshub.server.user.domain.usecase.RegisterUserUseCase
import io.vitalir.kotlinvcshub.server.user.domain.validation.UserValidationRule
import kotlin.random.Random

internal class RegisterUserUseCaseImpl(
    private val identifierValidationRule: UserValidationRule<UserCredentials.Identifier>,
    private val userPersistence: UserPersistence,
    private val passwordManager: PasswordManager,
) : RegisterUserUseCase {

    override suspend fun invoke(credentials: UserCredentials): Either<UserError, User> = either {
        validateCredentials(credentials).bind()
        checkIfUserExists(credentials.identifier).bind()
        val (login, email) = credentials.identifier.userIdentifiers
        val newUser = User(
            id = Random.nextInt(0, Int.MAX_VALUE), // TODO use UUID.generateRandom() or something like that
            login = login,
            password = credentials.password,
            email = email,
        )
        userPersistence.addUser(newUser)
        newUser
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

    companion object {

        private data class UserIdentifiers(
            val login: String,
            val email: String? = null,
        )

        private val UserCredentials.Identifier.userIdentifiers: UserIdentifiers
            get() = when (this) {
                is UserCredentials.Identifier.Email -> UserIdentifiers(login = this.value, email = this.value)
                is UserCredentials.Identifier.Login -> UserIdentifiers(login = this.value)
            }
    }
}
