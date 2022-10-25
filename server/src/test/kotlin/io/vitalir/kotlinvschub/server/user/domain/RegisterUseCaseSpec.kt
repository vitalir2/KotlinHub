package io.vitalir.kotlinvschub.server.user.domain

import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.email
import io.kotest.property.arbitrary.take
import io.mockk.confirmVerified
import io.mockk.spyk
import io.mockk.verify
import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvcshub.server.user.domain.model.UserCredentials
import io.vitalir.kotlinvcshub.server.user.domain.usecase.RegisterUserUseCase
import io.vitalir.kotlinvcshub.server.user.domain.usecase.impl.RegisterUserUseCaseImpl
import io.vitalir.kotlinvcshub.server.user.domain.validation.IdentifierValidationRule

class RegisterUseCaseSpec : ShouldSpec() {

    init {
        val anyUid = 123
        val anyPassword = "any"
        val spyIdentifierValidationRule = spyk(IdentifierValidationRule)

        val registerUserUseCase: RegisterUserUseCase = RegisterUserUseCaseImpl(
            identifierValidationRule = spyIdentifierValidationRule,
        )

        should("call identifier validation rule") {
            val email = UserCredentials.Identifier.Email("kek")
            val credentials = UserCredentials(
                identifier = email,
                password = anyPassword,
            )

            registerUserUseCase(credentials)

            verify { spyIdentifierValidationRule.validate(credentials.identifier) }
            confirmVerified(spyIdentifierValidationRule)
        }

        should("return user if user credentials with email are valid and user does not exist") {
            val randomEmails = Arb.email().take(10)
            val validPassword = "validPassword"
            for (email in randomEmails) {
                val credentials = UserCredentials(
                    identifier = UserCredentials.Identifier.Email(email),
                    password = validPassword,
                )
                val expectedUser = User(
                    id = anyUid,
                    login = email,
                    password = validPassword,
                    email = email,
                )

                val result = registerUserUseCase(credentials)

                val registeredUser: User = result.shouldBeRight()
                registeredUser.login shouldBe expectedUser.login
                registeredUser.email shouldBe expectedUser.email
                registeredUser.password shouldBe expectedUser.password
            }
        }
    }
}
