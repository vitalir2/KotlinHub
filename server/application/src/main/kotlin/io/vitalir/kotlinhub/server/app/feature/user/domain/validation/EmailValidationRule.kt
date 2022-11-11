package io.vitalir.kotlinhub.server.app.feature.user.domain.validation

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserError
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier

internal object EmailValidationRule : UserValidationRule<UserIdentifier.Email> {

    override fun validate(entity: UserIdentifier.Email): Either<UserError.ValidationFailed, Unit> {
        return if (EMAIL_REGEX.matches(entity.value)) {
            Unit.right()
        } else {
            UserError.ValidationFailed.left()
        }
    }

    private val EMAIL_REGEX = Regex("^[a-zA-Z0-9_!#\$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
}
