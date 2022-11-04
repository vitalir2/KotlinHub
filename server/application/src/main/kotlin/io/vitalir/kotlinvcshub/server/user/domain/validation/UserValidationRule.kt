package io.vitalir.kotlinvcshub.server.user.domain.validation

import arrow.core.Either
import io.vitalir.kotlinvcshub.server.user.domain.model.UserError

interface UserValidationRule<T> {

    fun validate(entity: T): Either<UserError.ValidationFailed, Unit>
}
