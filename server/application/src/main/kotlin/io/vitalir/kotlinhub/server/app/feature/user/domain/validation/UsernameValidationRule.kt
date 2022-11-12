package io.vitalir.kotlinhub.server.app.feature.user.domain.validation

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserError
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier

internal object UsernameValidationRule : UserValidationRule<UserIdentifier.Username> {

    override fun validate(entity: UserIdentifier.Username): Either<UserError.ValidationFailed, Unit> {
        return if (entity.value.length in USERNAME_LENGTH_RANGE) {
            Unit.right()
        } else {
            UserError.ValidationFailed.left()
        }
    }

    private val USERNAME_LENGTH_RANGE = 5..20
}
