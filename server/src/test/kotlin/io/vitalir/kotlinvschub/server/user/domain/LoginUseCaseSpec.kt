package io.vitalir.kotlinvschub.server.user.domain

import arrow.core.right
import arrow.core.some
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvcshub.server.user.domain.model.UserError
import io.vitalir.kotlinvcshub.server.user.domain.persistence.UserPersistence
import io.vitalir.kotlinvcshub.server.user.domain.usecase.LoginUseCase
import io.vitalir.kotlinvcshub.server.user.domain.usecase.impl.LoginUseCaseImpl

class LoginUseCaseSpec : ShouldSpec({
    val userPersistenceMock = mockk<UserPersistence>()
    val loginUseCase: LoginUseCase = LoginUseCaseImpl(
        userPersistence = userPersistenceMock,
    )
    val userLogin = "smock"
    val validUserIdentifier = User.Credentials.Identifier.Login(userLogin)
    val validHashedPassword = "some hashed value"
    val validUserCredentials = User.Credentials(
        identifier = validUserIdentifier,
        password = validHashedPassword
    )
    val someUser = User(
        id = 123,
        login = userLogin,
        password = validHashedPassword,
    )

    should("return user if the credentials with login are valid and it exists") {
        coEvery { userPersistenceMock.getUser(validUserIdentifier) } returns someUser.right()

        val loginResult = loginUseCase(validUserCredentials)

        loginResult.shouldBeRight() { error -> "should be right, but found left with error=$error" }
        loginResult.value shouldBe someUser
    }

    should("return user if the credentials with email are valid and it exists") {
        val validEmail = "heh123@gmail.com"
        val identifier = User.Credentials.Identifier.Email(validEmail)
        val credentials = User.Credentials(
            identifier = identifier,
            password = "any",
        )
        coEvery { userPersistenceMock.getUser(identifier) } returns someUser.right()

        val loginResult = loginUseCase(credentials)

        loginResult.shouldBeRight() { error -> "should be right, but found left with error=$error" }
        loginResult.value shouldBe someUser
    }

    should("return validation error if email is not valid") {
        val invalidEmail = "heh"
        val identifier = User.Credentials.Identifier.Email(invalidEmail)
        val credentials = User.Credentials(
            identifier = identifier,
            password = "any",
        )
        coEvery { userPersistenceMock.getUser(credentials.identifier) } returns someUser.right()

        val loginResult = loginUseCase(credentials)

        loginResult.shouldBeLeft()
        loginResult.value shouldBe UserError.ValidationFailed
    }
})
