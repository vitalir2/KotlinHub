package io.vitalir.kotlinhub.server.app.feature.repository

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.coEvery
import io.mockk.mockk
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.GetRepositoryUseCase
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl.GetRepositoryUseCaseImpl
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence
import java.time.LocalDate

internal class GetRepositoryUseCaseSpec : ShouldSpec() {

    private lateinit var repositoryPersistence: RepositoryPersistence

    private lateinit var userPersistence: UserPersistence

    private lateinit var getRepositoryUseCase: GetRepositoryUseCase

    init {
        val username = "megabrain123"
        val ownerId = 123
        val usernameIdentifier = UserIdentifier.Username(username)
        val owner = User(
            id = ownerId,
            username = username,
            password = "hellopassword",
        )
        val repositoryName = "repository"
        val createdAt = LocalDate.of(2022, 11, 15).atStartOfDay()
        val someRepositoryId = 123
        val repository = Repository(
            id = someRepositoryId,
            owner = owner,
            name = repositoryName,
            accessMode = Repository.AccessMode.PUBLIC,
            createdAt = createdAt,
            updatedAt = createdAt,
        )
        val privateRepository = Repository(
            id = someRepositoryId,
            owner = owner,
            name = repositoryName,
            accessMode = Repository.AccessMode.PRIVATE,
            createdAt = createdAt,
            updatedAt = createdAt,
        )

        beforeTest {
            repositoryPersistence = mockk()
            userPersistence = mockk()
            getRepositoryUseCase = GetRepositoryUseCaseImpl(
                repositoryPersistence = repositoryPersistence,
                userPersistence = userPersistence,
            )
        }

        should("return repository if it exists") {
            userExists(usernameIdentifier, owner)
            repositoryExistsForUser(usernameIdentifier, repository)

            val result = getRepositoryUseCase(
                userIdentifier = usernameIdentifier,
                repositoryName = repositoryName,
                currentUserId = null,
            )

            result shouldBeRight repository
        }

        should("return error if user does not exist") {
            coEvery {
                userPersistence.getUser(usernameIdentifier)
            } returns null

            val result = getRepositoryUseCase(
                userIdentifier = usernameIdentifier,
                repositoryName = repositoryName,
                currentUserId = null,
            )

            result shouldBeLeft GetRepositoryUseCase.Error.UserDoesNotExist(UserIdentifier.Username(username))
        }

        should("return error if repository does not exist") {
            userExists(usernameIdentifier, owner)
            coEvery {
                repositoryPersistence.getRepository(usernameIdentifier, repositoryName)
            } returns null

            val result = getRepositoryUseCase(
                userIdentifier = usernameIdentifier,
                repositoryName = repositoryName,
                currentUserId = null,
            )

            result shouldBeLeft GetRepositoryUseCase.Error.RepositoryDoesNotExist(
                UserIdentifier.Username(username), repositoryName
            )
        }

        should("return error if user has private repository and another tries to get it without permission") {
            userExists(usernameIdentifier, owner)
            repositoryExistsForUser(usernameIdentifier, privateRepository)

            val differentUsers = listOf(
                null,
                93,
            )
            for (currentUserId in differentUsers) {
                val result = getRepositoryUseCase(
                    userIdentifier = usernameIdentifier,
                    repositoryName = repositoryName,
                    currentUserId = currentUserId,
                )

                result shouldBeLeft GetRepositoryUseCase.Error.RepositoryDoesNotExist(
                    usernameIdentifier, repositoryName,
                )
            }
        }

        should("return private repository if user has access to it") {
            userExists(usernameIdentifier, owner)
            repositoryExistsForUser(usernameIdentifier, privateRepository)

            val result = getRepositoryUseCase(
                userIdentifier = usernameIdentifier,
                repositoryName = repositoryName,
                currentUserId = ownerId,
            )

            result shouldBeRight privateRepository
        }
    }

    private fun userExists(userIdentifier: UserIdentifier, user: User) {
        coEvery {
            userPersistence.getUser(userIdentifier)
        } returns user
    }

    private fun repositoryExistsForUser(
        userIdentifier: UserIdentifier,
        repository: Repository,
    ) {
        coEvery {
            repositoryPersistence.getRepository(userIdentifier, repository.name)
        } returns repository
    }
}
