package io.vitalir.kotlinvcshub.server.user.domain

import arrow.core.Either

interface LoginUseCase {

    suspend operator fun invoke(credentials: User.Credentials): Either<UserError, User>
}
