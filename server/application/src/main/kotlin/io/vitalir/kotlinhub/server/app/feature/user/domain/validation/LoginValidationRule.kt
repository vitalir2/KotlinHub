package io.vitalir.kotlinhub.server.app.feature.user.domain.validation

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserError
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier

internal object LoginValidationRule : UserValidationRule<UserIdentifier.Login> {

    override fun validate(entity: UserIdentifier.Login): Either<UserError.ValidationFailed, Unit> {
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
