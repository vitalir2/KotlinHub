package io.vitalir.kotlinhub.server.app.feature.repository

import arrow.core.left
import arrow.core.right
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryIdentifier
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.HasUserAccessToRepositoryUseCase
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl.HasUserAccessToRepositoryUseCaseImpl
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserCredentials
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserError
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence
import io.vitalir.kotlinhub.server.app.feature.user.domain.validation.UserValidationRule
import io.vitalir.kotlinhub.server.app.infrastructure.auth.PasswordManager
import java.time.LocalDateTime

internal class HasUserAccessToRepositorySpec : ShouldSpec() {

    private lateinit var userPersistence: UserPersistence

    private lateinit var repositoryPersistence: RepositoryPersistence

    private lateinit var passwordManager: PasswordManager

    private lateinit var userIdentifierValidationRule: UserValidationRule<UserIdentifier>

    private lateinit var hasUserAccessToRepositoryUseCase: HasUserAccessToRepositoryUseCase

    init {
        val someUserId = 123
        val somePassword = "somapassword"
        val someUser = User(
            id = someUserId,
            username = "someusername",
            password = somePassword,
        )
        val someUserCredentials = UserCredentials(
            identifier = UserIdentifier.Id(someUserId),
            password = somePassword,
        )
        val someRepositoryId = 124
        val someRepositoryIdentifier = RepositoryIdentifier.Id(someRepositoryId)
        val someRepository: (accessMode: Repository.AccessMode) -> Repository = { accessMode ->
            Repository(
                owner = someUser,
                id = someRepositoryId,
                name = "somereponame",
                accessMode = accessMode,
                createdAt = LocalDateTime.MIN,
                updatedAt = LocalDateTime.MIN,
            )
        }

        beforeTest {
            userPersistence = mockk()
            repositoryPersistence = mockk()
            passwordManager = mockk()
            userIdentifierValidationRule = mockk()
            hasUserAccessToRepositoryUseCase = HasUserAccessToRepositoryUseCaseImpl(
                userPersistence = userPersistence,
                repositoryPersistence = repositoryPersistence,
                passwordManager = passwordManager,
                userIdentifierValidationRule = userIdentifierValidationRule,
            )
        }

        should("return false if user credentials have invalid format") {
            val invalidUserIdentifiers = listOf(
                UserIdentifier.Username("I"),
                UserIdentifier.Username("Imverylonguseridentifiersoiwontpasstheverification"),
                UserIdentifier.Email("yes"),
                UserIdentifier.Email("youthinkimpassword@fool!"),
            )

            for (invalidUserIdentifier in invalidUserIdentifiers) {
                every {
                    userIdentifierValidationRule.validate(invalidUserIdentifier)
                } returns UserError.ValidationFailed.left()

                val result = hasUserAccessToRepositoryUseCase(
                    userCredentials = someUserCredentials.copy(identifier = invalidUserIdentifier),
                    repositoryIdentifier = someRepositoryIdentifier,
                )

                result shouldBe false
            }
        }

        should("return no access if repository does not exist") {
            userIdentifierIsValid(someUser.identifier)
            userExists(someUser)
            coEvery {
                repositoryPersistence.getRepository(someRepositoryIdentifier)
            } returns null

            val result = hasUserAccessToRepositoryUseCase(
                userCredentials = someUserCredentials,
                repositoryIdentifier = someRepositoryIdentifier,
            )

            result shouldBe false
        }

        should("return no access if repository is private and credentials are not valid") {
            userIdentifierIsValid(someUser.identifier)
            userExists(someUser)
            repositoryExists(someRepository(Repository.AccessMode.PRIVATE))
            val invalidPassword = "1"
            val invalidUserCredentials = someUserCredentials.copy(
                password = invalidPassword,
            )
            every {
                passwordManager.comparePasswords(invalidPassword, someUser.password)
            } returns false

            val result = hasUserAccessToRepositoryUseCase(
                userCredentials = invalidUserCredentials,
                repositoryIdentifier = someRepositoryIdentifier,
            )

            result shouldBe false
        }

        should("return has access if user is any and repo is public") {
            userIdentifierIsValid(someUser.identifier)
            userExists(someUser)
            repositoryExists(someRepository(Repository.AccessMode.PUBLIC))

            val result = hasUserAccessToRepositoryUseCase(
                userCredentials = someUserCredentials,
                repositoryIdentifier = someRepositoryIdentifier,
            )

            result shouldBe true
        }

        should("return has access if user is found and its credentials are valid") {
            userIdentifierIsValid(someUser.identifier)
            userExists(someUser)
            repositoryExists(someRepository(Repository.AccessMode.PRIVATE))
            passwordIsValid(someUserCredentials.password, someUser.password)

            val result = hasUserAccessToRepositoryUseCase(
                userCredentials = someUserCredentials,
                repositoryIdentifier = someRepositoryIdentifier,
            )

            result shouldBe true
        }
    }

    private fun userIdentifierIsValid(userIdentifier: UserIdentifier) {
        every {
            userIdentifierValidationRule.validate(userIdentifier)
        } returns Unit.right()
    }

    private fun userExists(user: User) {
        coEvery {
            userPersistence.getUser(user.identifier)
        } returns user
    }

    private fun repositoryExists(repository: Repository) {
        coEvery {
            repositoryPersistence.getRepository(RepositoryIdentifier.Id(repository.id))
        } returns repository
    }

    private fun passwordIsValid(givenPassword: String, hashedPassword: String) {
        every {
            passwordManager.comparePasswords(givenPassword, hashedPassword)
        } returns true
    }
}
