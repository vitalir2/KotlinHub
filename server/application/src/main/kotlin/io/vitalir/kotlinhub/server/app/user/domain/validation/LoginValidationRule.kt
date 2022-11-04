package io.vitalir.kotlinhub.server.app.user.domain.validation

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.vitalir.kotlinhub.server.app.user.domain.model.UserCredentials
import io.vitalir.kotlinhub.server.app.user.domain.model.UserError

internal object LoginValidationRule : UserValidationRule<UserCredentials.Identifier.Login> {

    override fun validate(entity: UserCredentials.Identifier.Login): Either<UserError.ValidationFailed, Unit> {
        return if (entity.value.length in LOGIN_LENGTH_RANGE) {
            Unit.right()
        } else {
            UserError.ValidationFailed.left()
        }
    }

    private const val LOGIN_MIN_LENGTH = 5
    private const val LOGIN_MAX_LENGTH = 20

    private val LOGIN_LENGTH_RANGE = LOGIN_MIN_LENGTH..LOGIN_MAX_LENGTH
}
