package io.vitalir.kotlinhub.server.app.feature.repository

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.RemoveRepositoryUseCase
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl.RemoveRepositoryUseCaseImpl
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence

internal class RemoveRepositoryUseCaseSpec : ShouldSpec() {

    private lateinit var userPersistence: UserPersistence

    private lateinit var repositoryPersistence: RepositoryPersistence

    private lateinit var removeRepositoryUseCase: RemoveRepositoryUseCase

    init {
        val someUserId = 123
        val someUserIdentifier = UserIdentifier.Id(someUserId)
        val someRepositoryName = "repositoryName"

        beforeTest {
            userPersistence = mockk()
            repositoryPersistence = spyk()
            removeRepositoryUseCase = RemoveRepositoryUseCaseImpl(
                userPersistence = userPersistence,
                repositoryPersistence = repositoryPersistence,
            )
        }

        should("return error if user does not exist") {
            coEvery {
                userPersistence.isUserExists(someUserIdentifier)
            } returns false

            val result = removeRepositoryUseCase(
                userId = someUserId,
                repositoryName = someRepositoryName,
            )

            result shouldBeLeft RemoveRepositoryUseCase.Error.UserDoesNotExist(someUserIdentifier)
        }

        should("return error if repository does not exist for the specific user") {
            coEvery {
                userPersistence.isUserExists(someUserIdentifier)
            } returns true
            coEvery {
                repositoryPersistence.isRepositoryExists(someUserId, someRepositoryName)
            } returns false

            val result = removeRepositoryUseCase(
                userId = someUserId,
                repositoryName = someRepositoryName,
            )

            result shouldBeLeft RemoveRepositoryUseCase.Error.RepositoryDoesNotExist(
                userIdentifier = someUserIdentifier,
                repositoryName = someRepositoryName,
            )
        }

        should("remove repository successfully if it exists for the specific user") {
            coEvery {
                userPersistence.isUserExists(someUserIdentifier)
            } returns true
            coEvery {
                repositoryPersistence.isRepositoryExists(someUserId, someRepositoryName)
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
