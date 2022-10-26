package io.vitalir.kotlinvcshub.server.user.domain.usecase

import arrow.core.Either
import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvcshub.server.user.domain.model.UserCredentials
import io.vitalir.kotlinvcshub.server.user.domain.model.UserError

interface LoginUseCase {

    suspend operator fun invoke(credentials: UserCredentials): Either<UserError, User>
}
