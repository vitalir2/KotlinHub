package io.vitalir.kotlinvcshub.server.user.domain.usecase

import arrow.core.Either
import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvcshub.server.user.domain.model.UserError

interface ValidateUserCredentialsUseCase {

    suspend operator fun invoke(userCredentials: User.Credentials): Either<UserError.ValidationFailed, Unit>
}
