package io.vitalir.kotlinvcshub.server.user.domain.persistence

import arrow.core.Either
import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvcshub.server.user.domain.model.UserError

interface UserPersistence {

    suspend fun getUser(identifier: User.Credentials.Identifier): Either<UserError.InvalidCredentials, User>

    suspend fun addUser(user: User): Either<UserError.UserAlreadyExists, Unit>
}
