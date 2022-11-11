package io.vitalir.kotlinhub.server.app.feature.user.domain.validation

import arrow.core.Either
import arrow.core.right
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserError
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier

internal object IdentifierValidationRule : UserValidationRule<UserIdentifier> {

    override fun validate(entity: UserIdentifier): Either<UserError.ValidationFailed, Unit> {
        return when (entity) {
            is UserIdentifier.Email -> EmailValidationRule.validate(entity)
            is UserIdentifier.Id -> Unit.right()
            is UserIdentifier.Login -> LoginValidationRule.validate(entity)
        }
    }
}
