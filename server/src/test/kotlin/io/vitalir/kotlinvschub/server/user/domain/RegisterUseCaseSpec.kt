package io.vitalir.kotlinvschub.server.user.domain

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvcshub.server.user.domain.model.UserCredentials
import io.vitalir.kotlinvcshub.server.user.domain.model.UserError
import io.vitalir.kotlinvcshub.server.user.domain.usecase.RegisterUserUseCase
import io.vitalir.kotlinvcshub.server.user.domain.usecase.impl.RegisterUserUseCaseImpl

class RegisterUseCaseSpec : ShouldSpec({
    val anyUid = 123
    val anyPassword = "any"

    val registerUserUseCase: RegisterUserUseCase = RegisterUserUseCaseImpl()

    should("return error if user email is invalid") {
        val email = UserCredentials.Identifier.Email("kek")
        val credentials = UserCredentials(
            identifier = email,
            password = anyPassword,
        )

        val result = registerUserUseCase(credentials)

        result.shouldBeLeft(UserError.InvalidCredentials)
    }

    should("return user if user credentials with email are valid and user does not exist") {
        val email = UserCredentials.Identifier.Email("g@x.ru")
        val validPassword = "validPassword"
        val credentials = UserCredentials(
            identifier = email,
            password = validPassword,
        )
        val expectedUser = User(
            id = anyUid,
            login = email.value,
            password = validPassword,
            email = email.value,
        )

        val result = registerUserUseCase(credentials)

        val registeredUser: User = result.shouldBeRight()
        registeredUser.login shouldBe expectedUser.login
        registeredUser.email shouldBe expectedUser.email
        registeredUser.password shouldBe expectedUser.password
    }
})
