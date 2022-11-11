package io.vitalir.kotlinhub.server.app.feature.user

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserCredentials
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserError
import io.vitalir.kotlinhub.server.app.infrastructure.auth.PasswordManager
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.RegisterUserUseCase
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.impl.RegisterUserUseCaseImpl
import io.vitalir.kotlinhub.server.app.feature.user.domain.validation.IdentifierValidationRule
import io.vitalir.kotlinhub.server.app.feature.user.domain.validation.UserValidationRule

class RegisterUseCaseSpec : ShouldSpec() {

    private lateinit var spyIdentifierValidationRule: UserValidationRule<UserCredentials.Identifier>

    private lateinit var spyUserPersistence: UserPersistence

    private lateinit var spyPasswordManager: PasswordManager

    private lateinit var registerUserUseCase: RegisterUserUseCase

    init {
        val anyUid = 123
        val anyString = "any"
        val validEmail = UserCredentials.Identifier.Email("gm@gmail.com")
        val validPassword = "validpassword"

        beforeEach {
            spyIdentifierValidationRule = spyk(IdentifierValidationRule)
            spyUserPersistence = spyk()
            spyPasswordManager = spyk()
            registerUserUseCase = RegisterUserUseCaseImpl(
                identifierValidationRule = spyIdentifierValidationRule,
                userPersistence = spyUserPersistence,
                passwordManager = spyPasswordManager,
            )
        }

        should("call identifier validation rule") {
            val email = UserCredentials.Identifier.Email(anyString)
            val credentials = UserCredentials(
                identifier = email,
                password = anyString,
            )

            registerUserUseCase(credentials)

            verify { spyIdentifierValidationRule.validate(credentials.identifier) }
        }

        should("return user already exists error if user with identifier exists") {
            val credentials = UserCredentials(
                identifier = validEmail,
                password = anyString,
            )
            coEvery { spyUserPersistence.isUserExists(credentials.identifier) } returns true
            setupSimplePasswordManager()

            val result = registerUserUseCase(credentials)

            result shouldBeLeft UserError.UserAlreadyExists
            coVerify { spyUserPersistence.addUser(any()) wasNot called }
        }

        should("return user if user credentials with email are valid and user does not exist") {
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
            coEvery { spyUserPersistence.isUserExists(credentials.identifier) } returns false
            setupSimplePasswordManager()

            val result = registerUserUseCase(credentials)

            val registeredUser: User = result.shouldBeRight()
            registeredUser.login shouldBe expectedUser.login
            registeredUser.email shouldBe expectedUser.email
            registeredUser.password shouldBe expectedUser.password
            confirmUserWasAddedCorrectly(credentials)
        }

        should("call adding user from UserPersistence if it does not exist yet") {
            val credentials = UserCredentials(
                identifier = validEmail,
                password = validPassword,
            )
            coEvery { spyUserPersistence.isUserExists(credentials.identifier) } returns false
            setupSimplePasswordManager()

            registerUserUseCase(credentials)

            confirmUserWasAddedCorrectly(credentials)
        }
    }

    private fun setupSimplePasswordManager() {
        every { spyPasswordManager.encode(any()) } returnsArgument 0
    }

    private fun confirmUserWasAddedCorrectly(
        credentials: UserCredentials,
    ) {
        coVerify { spyUserPersistence.addUser(any()) }
        verify { spyPasswordManager.encode(credentials.password) }
    }
}
