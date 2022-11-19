package io.vitalir.kotlinhub.server.app.feature.user.domain.persistence

import arrow.core.Either
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserError
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.shared.feature.user.UserId

interface UserPersistence {

    suspend fun getUser(identifier: UserIdentifier): User?

    suspend fun addUser(user: User): Either<UserError.UserAlreadyExists, UserId>

    suspend fun updateUsername(
        userId: UserId,
        username: String,
    ) : Either<UserError.InvalidCredentials, Unit>

    suspend fun updateEmail(
        userId: UserId,
        email: String,
    ) : Either<UserError.InvalidCredentials, Unit>

    suspend fun isUserExists(identifier: UserIdentifier): Boolean
    suspend fun removeUser(userId: UserId): Boolean
    suspend fun getUsers(): List<User>
}
