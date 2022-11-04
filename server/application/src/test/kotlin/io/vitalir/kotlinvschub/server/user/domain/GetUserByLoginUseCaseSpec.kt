package io.vitalir.kotlinvschub.server.user.domain

import arrow.core.left
import arrow.core.right
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.vitalir.kotlinhub.server.app.user.domain.model.User
import io.vitalir.kotlinhub.server.app.user.domain.model.UserCredentials
import io.vitalir.kotlinhub.server.app.user.domain.model.UserError
import io.vitalir.kotlinhub.server.app.user.domain.password.PasswordManager
import io.vitalir.kotlinhub.server.app.user.domain.persistence.UserPersistence
import io.vitalir.kotlinhub.server.app.user.domain.usecase.GetUserByLoginUseCase
import io.vitalir.kotlinhub.server.app.user.domain.usecase.impl.GetUserByLoginUseCaseImpl

internal class GetUserByLoginUseCaseSpec : ShouldSpec() {

    private lateinit var getUserByLoginUseCase: GetUserByLoginUseCase

    private lateinit var userPersistence: UserPersistence

    private val passwordManager: PasswordManager = mockk<PasswordManager>().apply {
        every { encode(any()) } returnsArgument 0
        every { comparePasswords(any(), any()) } returns true
    }

    private val UserCredentials.testValidUser: User
        get() = User.fromCredentials(this, passwordManager)

    init {

        val someValidLogin = UserCredentials.Identifier.Login("mamamia")
        val somePassword = "any"
        val someUserCredentials = UserCredentials(
            identifier = someValidLogin,
            password = somePassword,
        )

        beforeEach {
            userPersistence = spyk()
            getUserByLoginUseCase = GetUserByLoginUseCaseImpl(
                userPersistence = userPersistence,
            )
        }

        should("return user if it exists by login") {
            val expectedUser = someUserCredentials.testValidUser
            coEvery { userPersistence.getUser(someValidLogin) } returns expectedUser.right()

            val result = getUserByLoginUseCase(someValidLogin)

            result.shouldNotBeNull()
            result shouldHaveTheSameCredentialsAs expectedUser
        }

        should("call UserPersistence.getUser for getting user from persistence") {
            coEvery { userPersistence.getUser(someValidLogin) } returns someUserCredentials.testValidUser.right()

            getUserByLoginUseCase(someValidLogin)

            coVerify { userPersistence.getUser(someValidLogin) }
        }

        should("return null if user does not exist") {
            coEvery { userPersistence.getUser(someValidLogin) } returns UserError.InvalidCredentials.left()

            val result = getUserByLoginUseCase(someValidLogin)

            result.shouldBeNull()
        }
    }
}
