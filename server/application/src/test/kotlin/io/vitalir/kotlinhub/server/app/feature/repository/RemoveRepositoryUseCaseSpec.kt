package io.vitalir.kotlinhub.server.app.feature.repository

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.RemoveRepositoryUseCase
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl.RemoveRepositoryUseCaseImpl
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence
import io.vitalir.kotlinhub.server.app.infrastructure.git.GitManager

internal class RemoveRepositoryUseCaseSpec : ShouldSpec() {

    private lateinit var userPersistence: UserPersistence

    private lateinit var repositoryPersistence: RepositoryPersistence

    private lateinit var gitManager: GitManager

    private lateinit var removeRepositoryUseCase: RemoveRepositoryUseCase

    init {
        val someUserId = 123
        val someUserIdentifier = UserIdentifier.Id(someUserId)
        val someUser = User(
            id = someUserId,
            username = "someusername",
            password = "somepassword",
        )
        val someRepositoryName = "repositoryName"

        beforeTest {
            userPersistence = mockk()
            repositoryPersistence = spyk()
            gitManager = spyk()
            removeRepositoryUseCase = RemoveRepositoryUseCaseImpl(
                userPersistence = userPersistence,
                repositoryPersistence = repositoryPersistence,
                gitManager = gitManager,
            )
        }

        should("return error if user does not exist") {
            coEvery {
                userPersistence.getUser(someUserIdentifier)
            } returns null

            val result = removeRepositoryUseCase(
                userId = someUserId,
                repositoryName = someRepositoryName,
            )

            result shouldBeLeft RemoveRepositoryUseCase.Error.UserDoesNotExist(someUserIdentifier)
        }

        should("return error if repository does not exist for the specific user") {
            coEvery {
                userPersistence.getUser(someUserIdentifier)
            } returns someUser
            coEvery {
                repositoryPersistence.getRepository(someUserIdentifier, someRepositoryName)
            } returns null

            val result = removeRepositoryUseCase(
                userId = someUserId,
                repositoryName = someRepositoryName,
            )

            result shouldBeLeft RemoveRepositoryUseCase.Error.RepositoryDoesNotExist(
                userIdentifier = someUserIdentifier,
                repositoryName = someRepositoryName,
            )
        }

        should("return error if removal was not successful") {
            coEvery {
                userPersistence.getUser(someUserIdentifier)
            } returns someUser
            coEvery {
                repositoryPersistence.getRepository(someUserIdentifier, someRepositoryName)
            } returns Repository.any
            coEvery {
                gitManager.removeRepositoryByName(someUserId, someRepositoryName)
            } returns false

            val result = removeRepositoryUseCase(
                userId = someUserId,
                repositoryName = someRepositoryName,
            )

            result shouldBeLeft RemoveRepositoryUseCase.Error.Unknown
            coVerify(inverse = true) {
                repositoryPersistence.removeRepositoryByName(someUserId, someRepositoryName)
            }
        }

        should("remove repository successfully if it exists for the specific user") {
            coEvery {
                userPersistence.getUser(someUserIdentifier)
            } returns someUser
            coEvery {
                repositoryPersistence.getRepository(someUserIdentifier, someRepositoryName)
            } returns Repository.any
            coEvery {
                gitManager.removeRepositoryByName(someUserId, someRepositoryName)
            } returns true

            val result = removeRepositoryUseCase(
                userId = someUserId,
                repositoryName = someRepositoryName,
            )

            result shouldBeRight Unit
            coVerify {
                repositoryPersistence.removeRepositoryByName(someUserId, someRepositoryName)
            }
        }
    }
}
