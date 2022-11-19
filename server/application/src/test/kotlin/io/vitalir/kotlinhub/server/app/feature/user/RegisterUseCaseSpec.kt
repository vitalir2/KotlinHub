package io.vitalir.kotlinhub.server.app.feature.user

import arrow.core.left
import arrow.core.right
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
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.infrastructure.auth.PasswordManager
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.RegisterUserUseCase
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.impl.RegisterUserUseCaseImpl
import io.vitalir.kotlinhub.server.app.feature.user.domain.validation.IdentifierValidationRule
import io.vitalir.kotlinhub.server.app.feature.user.domain.validation.UserValidationRule

internal class RegisterUseCaseSpec : ShouldSpec() {

    private lateinit var spyIdentifierValidationRule: UserValidationRule<UserIdentifier>

    private lateinit var userPersistence: UserPersistence

    private lateinit var spyPasswordManager: PasswordManager

    private lateinit var registerUserUseCase: RegisterUserUseCase

    init {
        val anyUid = 123
        val anyString = "any"
        val validEmail = UserIdentifier.Email("gm@gmail.com")
        val validPassword = "validpassword"

        beforeEach {
            spyIdentifierValidationRule = spyk(IdentifierValidationRule)
            userPersistence = spyk()
            spyPasswordManager = spyk()
            registerUserUseCase = RegisterUserUseCaseImpl(
                identifierValidationRule = spyIdentifierValidationRule,
                userPersistence = userPersistence,
                passwordManager = spyPasswordManager,
            )
        }

        should("call identifier validation rule") {
            val email = UserIdentifier.Email(anyString)
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
            coEvery {
                userPersistence.isUserExists(credentials.identifier)
            } returns true
            setupSimplePasswordManager()

            val result = registerUserUseCase(credentials)

            result shouldBeLeft UserError.UserAlreadyExists
            coVerify { userPersistence.addUser(any()) wasNot called }
        }

        should("return error if user exists") {
            val credentials = UserCredentials(
                identifier = validEmail,
                password = anyString,
            )
            coEvery {
                userPersistence.isUserExists(credentials.identifier)
            } returns false
            coEvery {
                userPersistence.addUser(any())
            } returns UserError.UserAlreadyExists.left()
            setupSimplePasswordManager()

            val result = registerUserUseCase(credentials)

            result shouldBeLeft UserError.UserAlreadyExists
        }

        should("return user if user credentials with email are valid and user does not exist") {
            val credentials = UserCredentials(
                identifier = validEmail,
                password = validPassword,
            )
            val expectedId = 19421
            val expectedUser = User(
                id = expectedId,
                username = validEmail.value,
                password = validPassword,
                email = validEmail.value,
            )
            coEvery {
                userPersistence.isUserExists(credentials.identifier)
            } returns false
            coEvery {
                userPersistence.addUser(any())
            } returns expectedId.right()
            setupSimplePasswordManager()

            val result = registerUserUseCase(credentials)

            val registeredUser: User = result.shouldBeRight()
            registeredUser.id shouldBe expectedId
            registeredUser.username shouldBe expectedUser.username
            registeredUser.email shouldBe expectedUser.email
            registeredUser.password shouldBe expectedUser.password
        }

        should("call adding user from UserPersistence if it does not exist yet") {
            val credentials = UserCredentials(
                identifier = validEmail,
                password = validPassword,
            )
            coEvery {
                userPersistence.isUserExists(credentials.identifier)
            } returns false
            coEvery {
                userPersistence.addUser(any())
            } returns anyUid.right()
            setupSimplePasswordManager()

            registerUserUseCase(credentials)
        }
    }

    private fun setupSimplePasswordManager() {
        every { spyPasswordManager.encode(any()) } returnsArgument 0
    }
}
