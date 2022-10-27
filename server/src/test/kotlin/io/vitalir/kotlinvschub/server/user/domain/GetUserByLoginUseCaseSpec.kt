package io.vitalir.kotlinvschub.server.user.domain

import arrow.core.right
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvcshub.server.user.domain.model.UserCredentials
import io.vitalir.kotlinvcshub.server.user.domain.password.PasswordManager
import io.vitalir.kotlinvcshub.server.user.domain.persistence.UserPersistence
import io.vitalir.kotlinvcshub.server.user.domain.usecase.GetUserByLoginUseCase
import io.vitalir.kotlinvcshub.server.user.domain.usecase.impl.GetUserByLoginUseCaseImpl

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

        val anyPassword = "any"

        beforeEach {
            userPersistence = spyk()
            getUserByLoginUseCase = GetUserByLoginUseCaseImpl(
                userPersistence = userPersistence,
            )
        }

        should("return user if it exists by login") {
            val login = UserCredentials.Identifier.Login("mamamia")
            val userCredentials = UserCredentials(
                identifier = login,
                password = anyPassword,
            )
            val expectedUser = userCredentials.testValidUser
            coEvery { userPersistence.getUser(login) } returns expectedUser.right()

            val result = getUserByLoginUseCase(login)

            result.shouldNotBeNull()
            result shouldHaveTheSameCredentialsAs expectedUser
        }

        should("call UserPersistence.getUser for getting user from persistence") {
            val login = UserCredentials.Identifier.Login("mamamia")
            val userCredentials = UserCredentials(
                identifier = login,
                password = anyPassword,
            )
            coEvery { userPersistence.getUser(login) } returns userCredentials.testValidUser.right()

            getUserByLoginUseCase(login)

            coVerify { userPersistence.getUser(login) }
        }
    }
}
