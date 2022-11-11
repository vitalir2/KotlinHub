package io.vitalir.kotlinhub.server.app.feature.user.domain.validation

import arrow.core.Either
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserError

interface UserValidationRule<T> {

    fun validate(entity: T): Either<UserError.ValidationFailed, Unit>
}
