package io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.impl

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.left
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
        updateUsername(updateData.username, user)
        updateEmail(updateData.email, user)
    }

    private suspend fun updateEmail(
        emailWrapper: UpdateUserUseCase.UpdateData.Value<String>,
        user: User,
    ) {
        if (emailWrapper !is UpdateUserUseCase.UpdateData.Value.New<String>) return
        val email = emailWrapper.value
        userPersistence.updateEmail(user.id, email)
    }

    private suspend fun updateUsername(
        usernameWrapper: UpdateUserUseCase.UpdateData.Value<String>,
        user: User,
    ) {
        if (usernameWrapper !is UpdateUserUseCase.UpdateData.Value.New<String>) return
        val username = usernameWrapper.value
        userPersistence.updateUsername(user.id, username)
    }

    private suspend fun validateUpdateData(
        updateData: UpdateUserUseCase.UpdateData,
    ): Either<UpdateUserUseCase.Error.InvalidArguments, Unit> {
        return when {
            updateData.hasNoPotentialUpdates -> {
                UpdateUserUseCase.Error.InvalidArguments("No updates found").left()
            }
            else -> either {
                if (updateData.username is UpdateUserUseCase.UpdateData.Value.New<String>) {
                    val username = updateData.username.value
                    userValidationRule.validate(UserIdentifier.Username(username))
                        .mapLeft { UpdateUserUseCase.Error.InvalidArguments("username $username is not valid") }
                        .bind()
                }
                if (updateData.email is UpdateUserUseCase.UpdateData.Value.New<String>) {
                    val email = updateData.email.value
                    userValidationRule.validate(UserIdentifier.Email(email))
                        .mapLeft { UpdateUserUseCase.Error.InvalidArguments("email $email is not valid") }
                        .bind()
                }
            }
        }
    }
}
