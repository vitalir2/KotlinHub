package io.vitalir.kotlinvcshub.server.user.domain

import arrow.core.Either

interface UserPersistence {

    suspend fun getUser(identifier: User.Credentials.Identifier): Either<UserError.InvalidCredentials, User>

    suspend fun addUser(user: User): Either<UserError.UserAlreadyExists, Unit>
}
