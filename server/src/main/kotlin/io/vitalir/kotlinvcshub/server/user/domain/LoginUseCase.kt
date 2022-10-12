package io.vitalir.kotlinvcshub.server.user.domain

import arrow.core.Either

interface LoginUseCase {

    operator fun invoke(credentials: User.Credentials): Either<LoginError, User>
}
