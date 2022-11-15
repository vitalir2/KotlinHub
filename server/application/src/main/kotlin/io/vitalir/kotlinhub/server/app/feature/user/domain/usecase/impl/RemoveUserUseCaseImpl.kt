package io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.impl

import arrow.core.left
import arrow.core.right
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.RemoveUserResult
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.RemoveUserUseCase
import io.vitalir.kotlinhub.shared.feature.user.UserId

internal class RemoveUserUseCaseImpl(
    private val userPersistence: UserPersistence,
) : RemoveUserUseCase {

    override suspend fun invoke(userId: UserId): RemoveUserResult {
        return if (userPersistence.isUserExists(UserIdentifier.Id(userId))) {
            if (userPersistence.removeUser(userId)) {
                Unit.right()
            } else {
                RemoveUserUseCase.Error.RemoveFailed.left()
            }
        } else {
            RemoveUserUseCase.Error.UserDoesNotExist(userId).left()
        }
    }
}
