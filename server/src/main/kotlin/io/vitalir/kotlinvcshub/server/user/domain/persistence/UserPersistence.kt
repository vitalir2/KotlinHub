package io.vitalir.kotlinvcshub.server.user.domain.persistence

import arrow.core.Either
import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvcshub.server.user.domain.model.UserCredentials
import io.vitalir.kotlinvcshub.server.user.domain.model.UserError

interface UserPersistence {

    // TODO think about returning something else as error
    suspend fun getUser(identifier: UserCredentials.Identifier): Either<UserError.InvalidCredentials, User>

    suspend fun addUser(user: User): Either<UserError.UserAlreadyExists, Unit>
}
