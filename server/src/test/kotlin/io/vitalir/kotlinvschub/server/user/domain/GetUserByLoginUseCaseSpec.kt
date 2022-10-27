package io.vitalir.kotlinvschub.server.user.domain

import arrow.core.right
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvcshub.server.user.domain.model.UserCredentials
import io.vitalir.kotlinvcshub.server.user.domain.password.PasswordManager
import io.vitalir.kotlinvcshub.server.user.domain.persistence.UserPersistence
import io.vitalir.kotlinvcshub.server.user.domain.usecase.GetUserByLoginUseCase
import io.vitalir.kotlinvcshub.server.user.domain.usecase.impl.GetUserByLoginUseCaseImpl

class GetUserByLoginUseCaseSpec : ShouldSpec() {

    private lateinit var getUserByLoginUseCase: GetUserByLoginUseCase

    private lateinit var userPersistence: UserPersistence

    private val passwordManager: PasswordManager = mockk<PasswordManager>().apply {
        every { encode(any()) } returnsArgument 0
        every { comparePasswords(any(), any()) } returns true
    }

    init {

        beforeEach {
            userPersistence = mockk()
            getUserByLoginUseCase = GetUserByLoginUseCaseImpl(
                userPersistence = userPersistence,
            )
        }

        should("return user if it exists by login") {
            val login = UserCredentials.Identifier.Login("mamamia")
            val userCredentials = UserCredentials(
                identifier = login,
                password = "any",
            )
            val expectedUser = User.fromCredentials(userCredentials, passwordManager)
            coEvery { userPersistence.getUser(login) } returns expectedUser.right()

            val result = getUserByLoginUseCase(login)

            result.shouldNotBeNull()
            result shouldHaveTheSameCredentialsAs expectedUser
        }
    }
}
