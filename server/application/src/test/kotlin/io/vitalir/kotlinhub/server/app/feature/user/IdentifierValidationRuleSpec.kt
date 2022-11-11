package io.vitalir.kotlinhub.server.app.feature.user

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.email
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserCredentials
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserError
import io.vitalir.kotlinhub.server.app.feature.user.domain.validation.IdentifierValidationRule
import io.vitalir.kotlinhub.server.app.feature.user.domain.validation.UserValidationRule

class IdentifierValidationRuleSpec : ShouldSpec() {

    init {
        val identifierValidationRule: UserValidationRule<UserCredentials.Identifier> = IdentifierValidationRule

        should("validate login correctly") {
            val randomLogins = Arb.string()
                .map { UserCredentials.Identifier.Login(it) }
            checkAll(randomLogins) { login ->
                val validationResult = identifierValidationRule.validate(login)
                if (login.value.length in 5..20) {
                    validationResult shouldBeRight Unit
                } else {
                    validationResult shouldBeLeft UserError.ValidationFailed
                }
            }
        }

        should("validate email correctly") {
            val validRegex = Regex("^[a-zA-Z0-9_!#\$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
            val randomEmails = Arb.email()
                .map { UserCredentials.Identifier.Email(it) }
            checkAll(randomEmails) { email ->
                val validationResult = identifierValidationRule.validate(email)
                if (validRegex.matches(email.value)) {
                    validationResult shouldBeRight Unit
                } else {
                    validationResult shouldBeLeft UserError.ValidationFailed
                }
            }
        }
    }
}
