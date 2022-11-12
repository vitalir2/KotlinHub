package io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.impl

import arrow.core.Either
import arrow.core.continuations.EffectScope
import arrow.core.continuations.either
import arrow.core.left
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserId
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.UpdateUserResult
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.UpdateUserUseCase
import io.vitalir.kotlinhub.server.app.feature.user.domain.validation.UserValidationRule

internal class UpdateUserUseCaseImpl(
    private val userPersistence: UserPersistence,
    private val userValidationRule: UserValidationRule<UserIdentifier>,
) : UpdateUserUseCase {

    override suspend fun invoke(
        userId: UserId,
        username: String?,
        email: String?,
    ): UpdateUserResult = either {
        when {
            userPersistence.isUserExists(UserIdentifier.Id(userId)).not() -> {
                UpdateUserUseCase.Error.NoUser(userId).left().bind()
            }
            username != null && email != null -> {
                validOrReturnError(username, ::isUsernameValid) {
                    userPersistence.updateUsername(userId, username)
                }
                validOrReturnError(email, ::isEmailValid) {
                    userPersistence.updateEmail(userId, email)
                }
            }
            username != null -> {
                validOrReturnError(username, ::isUsernameValid) {
                    userPersistence.updateUsername(userId, username)
                }
            }
            email != null -> {
                validOrReturnError(email, ::isEmailValid) {
                    userPersistence.updateEmail(userId, email)
                }
            }
            else -> {
                val error: Either<UpdateUserUseCase.Error, Unit> =
                    UpdateUserUseCase.Error.InvalidArguments("Both arguments are null").left()
                error.bind()
            }
        }
    }

    private suspend inline fun EffectScope<UpdateUserUseCase.Error>.validOrReturnError(
        value: String,
        validate: (value: String) -> Boolean,
        onValidationPassed: () -> Unit,
    ) {
        if (validate(value)) {
            onValidationPassed()
        } else {
            val error: Either<UpdateUserUseCase.Error.InvalidArguments, Unit> =
                UpdateUserUseCase.Error.InvalidArguments("value=$value is not valid").left()
            error.bind()
        }
    }

    private fun isUsernameValid(username: String): Boolean {
        return userValidationRule.validate(UserIdentifier.Username(username)).isRight()
    }

    private fun isEmailValid(email: String): Boolean {
        return userValidationRule.validate(UserIdentifier.Email(email)).isRight()
    }
}
