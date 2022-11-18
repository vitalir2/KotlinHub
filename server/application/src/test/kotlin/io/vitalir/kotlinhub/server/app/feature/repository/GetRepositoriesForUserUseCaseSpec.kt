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
        beforeTest {
            userPersistence = mockk()
            repositoryPersistence = mockk()
            getRepositoriesForUserUseCase = GetRepositoriesForUserUseCaseImpl(
                userPersistence = userPersistence,
                repositoryPersistence = repositoryPersistence,
            )
        }

        should("return error if user does not exist") {
            val userId = 123
            val userIdentifier = UserIdentifier.Id(userId)
            userDoesNotExist(userIdentifier)

            val result = getRepositoriesForUserUseCase(
                userIdentifier = userIdentifier,
            )

            result shouldBeLeft GetRepositoriesForUserUseCase.Error.UserDoesNotExist(userIdentifier)
        }

        should("return repositories for user which exists") {
            val userId = 123
            val userIdentifier = UserIdentifier.Id(userId)
            val repositories = listOf(
                sampleRepository(userId, "repositoryOne"),
                sampleRepository(userId, "repositoryTwo"),
            )
            userExists(userIdentifier)
            coEvery {
                repositoryPersistence.getRepositories(userIdentifier)
            } returns repositories

            val result = getRepositoriesForUserUseCase(
                userIdentifier = userIdentifier,
            )

            result shouldBeRight repositories
        }
    }

    private fun sampleRepository(userId: UserId, repositoryName: String): Repository {
        return Repository(
            id = repositoryIdIncrement++,
            owner = User(
                id = userId,
                username = "someusername",
                password = "somepassword",
            ),
            name = repositoryName,
            accessMode = Repository.AccessMode.PUBLIC,
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

    companion object {
        private var repositoryIdIncrement = 0
    }
}
