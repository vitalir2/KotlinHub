package io.vitalir.kotlinvcshub.server.user.domain.validation

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvcshub.server.user.domain.model.UserError

internal object EmailValidationRule : UserValidationRule<User.Credentials.Identifier.Email> {

    override fun validate(entity: User.Credentials.Identifier.Email): Either<UserError.ValidationFailed, Unit> {
        return if (EMAIL_REGEX.matches(entity.value)) {
            Unit.right()
        } else {
            UserError.ValidationFailed.left()
        }
    }

    private val EMAIL_REGEX = Regex("^[a-zA-Z0-9_!#\$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
}
