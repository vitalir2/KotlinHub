package io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.impl

import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.GetUserByIdentifierUseCase

internal class GetUserByIdentifierUseCaseImpl(
    private val userPersistence: UserPersistence,
) : GetUserByIdentifierUseCase {

    override suspend fun invoke(userIdentifier: UserIdentifier): User? {
        return userPersistence.getUser(userIdentifier)
    }
}
