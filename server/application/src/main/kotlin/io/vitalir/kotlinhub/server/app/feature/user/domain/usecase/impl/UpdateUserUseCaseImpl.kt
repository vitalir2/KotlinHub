package io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.impl

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import arrow.core.rightIfNotNull
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.UpdateUserResult
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.UpdateUserUseCase
import io.vitalir.kotlinhub.server.app.feature.user.domain.validation.UserValidationRule
import io.vitalir.kotlinhub.shared.feature.user.UserId

internal class UpdateUserUseCaseImpl(
    private val userPersistence: UserPersistence,
    private val userValidationRule: UserValidationRule<UserIdentifier>,
) : UpdateUserUseCase {

    override suspend fun invoke(
        userId: UserId,
        updateData: UpdateUserUseCase.UpdateData,
    ): UpdateUserResult = either {
        validateUpdateData(updateData).bind()
        val user = userPersistence.getUser(UserIdentifier.Id(userId)).rightIfNotNull {
            UpdateUserUseCase.Error.NoUser(userId)
        }.bind()
        updateUsername(updateData.username, user).bind()
        updateEmail(updateData.email, user).bind()
    }

    private suspend fun updateEmail(
        emailWrapper: UpdateUserUseCase.UpdateData.Value<String>,
        user: User,
    ): Either<UpdateUserUseCase.Error.Conflict, Unit> {
        if (emailWrapper !is UpdateUserUseCase.UpdateData.Value.New<String>) {
            return Unit.right()
        }

        val email = emailWrapper.value
        val isUserWithNewEmailExists = userPersistence.isUserExists(UserIdentifier.Email(email))
        return if (isUserWithNewEmailExists) {
            UpdateUserUseCase.Error.Conflict("email" to email).left()
        } else {
            userPersistence.updateEmail(user.id, email)
            Unit.right()
        }
    }

    private suspend fun updateUsername(
        usernameWrapper: UpdateUserUseCase.UpdateData.Value<String>,
        user: User,
    ): Either<UpdateUserUseCase.Error.Conflict, Unit> {
        if (usernameWrapper !is UpdateUserUseCase.UpdateData.Value.New<String>) {
            return Unit.right()
        }

        val username = usernameWrapper.value
        val isUserWithNewUsernameExists = userPersistence.isUserExists(UserIdentifier.Username(username))
        return if (isUserWithNewUsernameExists) {
            UpdateUserUseCase.Error.Conflict("username" to username).left()
        } else {
            userPersistence.updateUsername(user.id, username)
            Unit.right()
        }
    }

    private suspend fun validateUpdateData(
        updateData: UpdateUserUseCase.UpdateData,
    ): Either<UpdateUserUseCase.Error, Unit> {
        return when {
            updateData.hasNoPotentialUpdates -> {
                UpdateUserUseCase.Error.NoUpdates.left()
            }
            else -> either {
                if (updateData.username is UpdateUserUseCase.UpdateData.Value.New<String>) {
                    validateUsername(updateData.username.value).bind()
                }
                if (updateData.email is UpdateUserUseCase.UpdateData.Value.New<String>) {
                    validateEmail(updateData.email.value).bind()
                }
            }
        }
    }

    private fun validateUsername(
        username: String,
    ): Either<UpdateUserUseCase.Error.InvalidArgument, Unit> {
        return userValidationRule.validate(UserIdentifier.Username(username))
            .mapLeft { UpdateUserUseCase.Error.InvalidArgument("username" to username) }
    }

    private fun validateEmail(
        email: String,
    ): Either<UpdateUserUseCase.Error.InvalidArgument, Unit> {
        return userValidationRule.validate(UserIdentifier.Email(email))
            .mapLeft { UpdateUserUseCase.Error.InvalidArgument("email" to email) }
    }
}
