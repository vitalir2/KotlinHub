package io.vitalir.kotlinhub.server.app.feature.repository

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.coEvery
import io.mockk.mockk
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.GetRepositoriesForUserUseCase
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl.GetRepositoriesForUserUseCaseImpl
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence
import io.vitalir.kotlinhub.shared.feature.user.UserId
import java.time.LocalDateTime

internal class GetRepositoriesForUserUseCaseSpec : ShouldSpec() {

    private lateinit var userPersistence: UserPersistence

    private lateinit var repositoryPersistence: RepositoryPersistence

    private lateinit var getRepositoriesForUserUseCase: GetRepositoriesForUserUseCase

    init {
        val sampleUserId = 123
        val sampleUserIdentifier = UserIdentifier.Id(sampleUserId)

        beforeTest {
            repositoryIdIncrement = 0
            userPersistence = mockk()
            repositoryPersistence = mockk()
            getRepositoriesForUserUseCase = GetRepositoriesForUserUseCaseImpl(
                userPersistence = userPersistence,
                repositoryPersistence = repositoryPersistence,
            )
        }

        should("return error if user does not exist") {
            userDoesNotExist(sampleUserIdentifier)

            val result = getRepositoriesForUserUseCase(
                currentUserId = null,
                userIdentifier = sampleUserIdentifier,
            )

            result shouldBeLeft GetRepositoriesForUserUseCase.Error.UserDoesNotExist(sampleUserIdentifier)
        }

        should("return all public repositories for user which exists") {
            val repositories = listOf(
                sampleRepository(sampleUserId, "repositoryOne"),
                sampleRepository(sampleUserId, "repositoryTwo"),
            )
            userExists(sampleUserIdentifier)
            userHasRepositories(sampleUserIdentifier, repositories, listOf(Repository.AccessMode.PUBLIC))

            val result = getRepositoriesForUserUseCase(
                currentUserId = null,
                userIdentifier = sampleUserIdentifier,
            )

            result shouldBeRight repositories
        }

        should("return only public repositories for user without login") {
            val userRepositories = listOf(
                sampleRepository(sampleUserId),
                sampleRepository(sampleUserId, accessMode = Repository.AccessMode.PRIVATE),
            )
            userExists(sampleUserIdentifier)
            userHasRepositories(sampleUserIdentifier, userRepositories, listOf(Repository.AccessMode.PUBLIC))

            val result = getRepositoriesForUserUseCase(
                currentUserId = null,
                userIdentifier = sampleUserIdentifier,
            )

            // Only first one is not private
            result shouldBeRight userRepositories.subList(0, 1)
        }

        should("return only public user for user that does not have access to another user repositories") {
            val userRepositories = listOf(
                sampleRepository(sampleUserId),
                sampleRepository(sampleUserId, accessMode = Repository.AccessMode.PRIVATE),
            )
            userExists(sampleUserIdentifier)
            userHasRepositories(
                userIdentifier = sampleUserIdentifier,
                repositories = userRepositories,
                accessibleRepositories = listOf(Repository.AccessMode.PUBLIC),
            )

            val result = getRepositoriesForUserUseCase(
                currentUserId = 292,
                userIdentifier = sampleUserIdentifier,
            )

            // Only first one is not private
            result shouldBeRight userRepositories.subList(0, 1)
        }

        should("return all repositories for user which asks its own repos") {
            val userRepositories = listOf(
                sampleRepository(sampleUserId),
                sampleRepository(sampleUserId, accessMode = Repository.AccessMode.PRIVATE),
            )
            userExists(sampleUserIdentifier)
            userHasRepositories(
                userIdentifier = sampleUserIdentifier,
                repositories = userRepositories,
                accessibleRepositories = listOf(Repository.AccessMode.PUBLIC, Repository.AccessMode.PRIVATE),
            )

            val result = getRepositoriesForUserUseCase(
                currentUserId = sampleUserId,
                userIdentifier = sampleUserIdentifier,
            )

            result shouldBeRight userRepositories
        }
    }

    private fun sampleRepository(
        userId: UserId,
        repositoryName: String = "repository$repositoryIdIncrement",
        accessMode: Repository.AccessMode = Repository.AccessMode.PUBLIC,
    ): Repository {
        return Repository(
            id = repositoryIdIncrement++,
            owner = User(
                id = userId,
                username = "someusername",
                password = "somepassword",
            ),
            name = repositoryName,
            accessMode = accessMode,
            createdAt = LocalDateTime.MIN,
            updatedAt = LocalDateTime.MIN,
        )
    }

    private fun userExists(userIdentifier: UserIdentifier) {
        coEvery {
            userPersistence.getUser(userIdentifier)
        } returns User(
            id = (userIdentifier as? UserIdentifier.Id)?.value ?: 123,
            username = (userIdentifier as? UserIdentifier.Username)?.value ?: "someusername",
            password = "helloworld",
        )
    }

    private fun userDoesNotExist(userIdentifier: UserIdentifier) {
        coEvery {
            userPersistence.getUser(userIdentifier)
        } returns null
    }

    private fun userHasRepositories(
        userIdentifier: UserIdentifier,
        repositories: List<Repository>,
        accessibleRepositories: List<Repository.AccessMode>,
    ) {
        coEvery {
            repositoryPersistence.getRepositories(userIdentifier, accessibleRepositories)
        } returns repositories.filter { it.accessMode in accessibleRepositories }
    }

    companion object {
        private var repositoryIdIncrement = 0
    }
}
