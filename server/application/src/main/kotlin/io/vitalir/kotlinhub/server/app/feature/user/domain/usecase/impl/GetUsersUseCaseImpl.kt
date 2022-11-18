package io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.impl

import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.GetUsersUseCase

internal class GetUsersUseCaseImpl(
    private val userPersistence: UserPersistence,
) : GetUsersUseCase {
    override suspend fun invoke(): List<User> {
        return userPersistence.getUsers()
    }
}
