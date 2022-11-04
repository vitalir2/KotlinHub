package io.vitalir.kotlinvcshub.server.user.domain.validation

import arrow.core.Either
import io.vitalir.kotlinvcshub.server.user.domain.model.UserCredentials
import io.vitalir.kotlinvcshub.server.user.domain.model.UserError

internal object IdentifierValidationRule : UserValidationRule<UserCredentials.Identifier> {

    override fun validate(entity: UserCredentials.Identifier): Either<UserError.ValidationFailed, Unit> {
        return when (entity) {
            is UserCredentials.Identifier.Email -> EmailValidationRule.validate(entity)
            is UserCredentials.Identifier.Login -> LoginValidationRule.validate(entity)
        }
    }
}
