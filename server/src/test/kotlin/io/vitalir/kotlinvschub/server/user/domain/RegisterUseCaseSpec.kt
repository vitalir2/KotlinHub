package io.vitalir.kotlinvschub.server.user.domain

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvcshub.server.user.domain.model.UserError
import io.vitalir.kotlinvcshub.server.user.domain.usecase.RegisterUserUseCase
import io.vitalir.kotlinvcshub.server.user.domain.usecase.impl.RegisterUserUseCaseImpl

class RegisterUseCaseSpec : ShouldSpec({
    val anyPassword = "any"

    val registerUserUseCase: RegisterUserUseCase = RegisterUserUseCaseImpl()

    should("return error if user email is invalid") {
        val email = User.Credentials.Identifier.Email("kek")
        val credentials = User.Credentials(
            identifier = email,
            password = anyPassword,
        )

        val result = registerUserUseCase(credentials)

        result.shouldBeLeft()
        result.value shouldBe UserError.InvalidCredentials
    }
})
