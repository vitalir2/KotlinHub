package io.vitalir.kotlinhub.server.app.feature.user.domain.persistence

import arrow.core.Either
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserError
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier

interface UserPersistence {

    suspend fun getUser(identifier: UserIdentifier): User?

    suspend fun addUser(user: User): Either<UserError.UserAlreadyExists, Unit>

    suspend fun updateUser(user: User) : Either<UserError.InvalidCredentials, Unit>

    suspend fun isUserExists(identifier: UserIdentifier): Boolean
}
