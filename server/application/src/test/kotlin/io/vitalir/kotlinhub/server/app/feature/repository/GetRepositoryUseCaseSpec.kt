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
        val usernameIdentifier = UserIdentifier.Username(username)
        val owner = User(
            id = 123,
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

        beforeEach {
            repositoryPersistence = mockk()
            userPersistence = mockk()
            getRepositoryUseCase = GetRepositoryUseCaseImpl(
                repositoryPersistence = repositoryPersistence,
                userPersistence = userPersistence,
            )
        }

        should("return repository if it exists") {
            coEvery { userPersistence.isUserExists(usernameIdentifier) } returns true
            coEvery { repositoryPersistence.getRepository(usernameIdentifier, repositoryName) } returns repository

            val result = getRepositoryUseCase(usernameIdentifier, repositoryName)

            result shouldBeRight repository
        }

        should("return error if user does not exist") {
            coEvery { userPersistence.isUserExists(usernameIdentifier) } returns false

            val result = getRepositoryUseCase(usernameIdentifier, repositoryName)

            result shouldBeLeft GetRepositoryUseCase.Error.UserDoesNotExist(UserIdentifier.Username(username))
        }

        should("return error if repository does not exist") {
            coEvery { userPersistence.isUserExists(usernameIdentifier) } returns true
            coEvery { repositoryPersistence.getRepository(usernameIdentifier, repositoryName) } returns null

            val result = getRepositoryUseCase(usernameIdentifier, repositoryName)

            result shouldBeLeft GetRepositoryUseCase.Error.RepositoryDoesNotExist(
                UserIdentifier.Username(username), repositoryName
            )
        }
    }
}
