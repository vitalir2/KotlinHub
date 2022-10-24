package io.vitalir.kotlinvschub.server.user.domain

import arrow.core.right
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import io.vitalir.kotlinvcshub.server.user.domain.model.User
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

    should("return user if the credentials are valid and it exists") {
        coEvery { userPersistenceMock.getUser(validUserIdentifier) } returns someUser.right()

        val loginResult = loginUseCase(validUserCredentials)

        loginResult.shouldBeRight() { error -> "should be right, but found left with error=$error" }
        loginResult.value shouldBe someUser
    }
})
