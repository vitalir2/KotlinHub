package io.vitalir.kotlinvcshub.server.user.domain.persistence

import arrow.core.Either
import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvcshub.server.user.domain.model.UserCredentials
import io.vitalir.kotlinvcshub.server.user.domain.model.UserError
import io.vitalir.kotlinvcshub.server.user.domain.model.UserId

interface UserPersistence {

    // TODO think about returning something else as error
    suspend fun getUser(identifier: UserCredentials.Identifier): Either<UserError.InvalidCredentials, User>

    suspend fun getUser(userId: UserId): User?

    suspend fun addUser(user: User): Either<UserError.UserAlreadyExists, Unit>

    suspend fun isUserExists(identifier: UserCredentials.Identifier): Boolean

    suspend fun isUserExists(userId: UserId): Boolean
}
