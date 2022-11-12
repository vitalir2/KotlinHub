package io.vitalir.kotlinhub.server.app.feature.user

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserCredentials
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.GetUserByIdentifierUseCase
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.impl.GetUserByIdentifierUseCaseImpl
import io.vitalir.kotlinhub.server.app.infrastructure.auth.PasswordManager

internal class GetUserByLoginUseCaseSpec : ShouldSpec() {

    private lateinit var getUserByIdentifierUseCase: GetUserByIdentifierUseCase

    private lateinit var userPersistence: UserPersistence

    private val passwordManager: PasswordManager = mockk<PasswordManager>().apply {
        every { encode(any()) } returnsArgument 0
        every { comparePasswords(any(), any()) } returns true
    }

    private val UserCredentials.testValidUser: User
        get() = User.fromCredentials(this, passwordManager)

    init {

        val someValidUsername = UserIdentifier.Username("mamamia")
        val somePassword = "any"
        val someUserCredentials = UserCredentials(
            identifier = someValidUsername,
            password = somePassword,
        )

        beforeEach {
            userPersistence = spyk()
            getUserByIdentifierUseCase = GetUserByIdentifierUseCaseImpl(
                userPersistence = userPersistence,
            )
        }

        should("return user if it exists by login") {
            val expectedUser = someUserCredentials.testValidUser
            coEvery { userPersistence.getUser(someValidUsername) } returns expectedUser

            val result = getUserByIdentifierUseCase(someValidUsername)

            result.shouldNotBeNull()
            result shouldHaveTheSameCredentialsAs expectedUser
        }

        should("call UserPersistence.getUser for getting user from persistence") {
            coEvery { userPersistence.getUser(someValidUsername) } returns someUserCredentials.testValidUser

            getUserByIdentifierUseCase(someValidUsername)

            coVerify { userPersistence.getUser(someValidUsername) }
        }

        should("return null if user does not exist") {
            coEvery { userPersistence.getUser(someValidUsername) } returns null

            val result = getUserByIdentifierUseCase(someValidUsername)

            result.shouldBeNull()
        }
    }
}
