package io.vitalir.kotlinvschub.server.user.domain

import arrow.core.right
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvcshub.server.user.domain.model.UserCredentials
import io.vitalir.kotlinvcshub.server.user.domain.model.UserError
import io.vitalir.kotlinvcshub.server.user.domain.persistence.UserPersistence
import io.vitalir.kotlinvcshub.server.user.domain.usecase.RegisterUserUseCase
import io.vitalir.kotlinvcshub.server.user.domain.usecase.impl.RegisterUserUseCaseImpl
import io.vitalir.kotlinvcshub.server.user.domain.validation.IdentifierValidationRule

class RegisterUseCaseSpec : ShouldSpec() {

    init {
        val anyUid = 123
        val anyString = "any"
        val validEmail = UserCredentials.Identifier.Email("gm@gmail.com")
        val spyIdentifierValidationRule = spyk(IdentifierValidationRule)
        val mockUserPersistence = mockk<UserPersistence>()

        val registerUserUseCase: RegisterUserUseCase = RegisterUserUseCaseImpl(
            identifierValidationRule = spyIdentifierValidationRule,
            userPersistence = mockUserPersistence,
        )

        should("call identifier validation rule") {
            val email = UserCredentials.Identifier.Email(anyString)
            val credentials = UserCredentials(
                identifier = email,
                password = anyString,
            )

            registerUserUseCase(credentials)

            verify { spyIdentifierValidationRule.validate(credentials.identifier) }
            confirmVerified(spyIdentifierValidationRule)
        }

        should("return user already exists error if user with identifier exists") {
            val credentials = UserCredentials(
                identifier = validEmail,
                password = anyString,
            )
            coEvery { mockUserPersistence.getUser(credentials.identifier) } returns User.any.right()

            val result = registerUserUseCase(credentials)

            result shouldBeLeft UserError.UserAlreadyExists
        }

        should("return user if user credentials with email are valid and user does not exist") {
            val validPassword = "validPassword"
            val credentials = UserCredentials(
                identifier = validEmail,
                password = validPassword,
            )
            val expectedUser = User(
                id = anyUid,
                login = validEmail.value,
                password = validPassword,
                email = validEmail.value,
            )

            val result = registerUserUseCase(credentials)

            val registeredUser: User = result.shouldBeRight()
            registeredUser.login shouldBe expectedUser.login
            registeredUser.email shouldBe expectedUser.email
            registeredUser.password shouldBe expectedUser.password
        }
    }
}
