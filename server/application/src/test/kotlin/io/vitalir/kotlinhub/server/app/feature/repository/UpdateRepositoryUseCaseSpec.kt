package io.vitalir.kotlinhub.server.app.feature.repository

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.UpdateRepositoryData
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.UpdateRepositoryUseCase
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl.UpdateRepositoryUseCaseImpl
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence
import java.time.LocalDateTime

internal class UpdateRepositoryUseCaseSpec : ShouldSpec() {

    private lateinit var userPersistence: UserPersistence

    private lateinit var repositoryPersistence: RepositoryPersistence

    private lateinit var updateRepositoryUseCase: UpdateRepositoryUseCase

    init {
        beforeTest {
            userPersistence = mockk()
            repositoryPersistence = mockk()
            updateRepositoryUseCase = UpdateRepositoryUseCaseImpl(
                userPersistence = userPersistence,
                repositoryPersistence = repositoryPersistence,
            )
        }

        val someUserId = 123
        val someUserIdentifier = UserIdentifier.Id(someUserId)
        val someRepositoryName = "helloworld"
        val someUpdateRepositoryData = UpdateRepositoryData(accessMode = Repository.AccessMode.PRIVATE)

        should("return error if user does not exist") {
            val userId = 132
            val userIdentifier = UserIdentifier.Id(userId)
            coEvery {
                userPersistence.isUserExists(userIdentifier)
            } returns false

            val result = updateRepositoryUseCase(
                userIdentifier = userIdentifier,
                repositoryName = someRepositoryName,
                updateRepositoryData = someUpdateRepositoryData,
            )

            result shouldBeLeft UpdateRepositoryUseCase.Error.UserDoesNotExist(userIdentifier)
            coVerify(inverse = true) {
                repositoryPersistence.updateRepository(any(), any(), any())
            }
        }

        should("return error if repository for user does not exist") {
            val repositoryName = "reponame"
            coEvery {
                userPersistence.isUserExists(someUserIdentifier)
            } returns true
            coEvery {
                repositoryPersistence.isRepositoryExists(someUserIdentifier, repositoryName)
            } returns false

            val result = updateRepositoryUseCase(
                userIdentifier = someUserIdentifier,
                repositoryName = repositoryName,
                updateRepositoryData = someUpdateRepositoryData,
            )

            result shouldBeLeft UpdateRepositoryUseCase.Error.RepositoryDoesNotExist(
                userIdentifier = someUserIdentifier,
                repositoryName = repositoryName,
            )
            coVerify(inverse = true) {
                repositoryPersistence.updateRepository(any(), any(), any())
            }
        }

        should("return success if repository for user exists and repository name update successful") {
            coEvery {
                userPersistence.isUserExists(someUserIdentifier)
            } returns true
            coEvery {
                repositoryPersistence.isRepositoryExists(someUserIdentifier, someRepositoryName)
            } returns true
            coEvery {
                repositoryPersistence.updateRepository(any(), any(), any())
            } returns Unit

            val updateRepositoryDataList = listOf(
                UpdateRepositoryData(accessMode = Repository.AccessMode.PRIVATE),
                UpdateRepositoryData(updatedAt = LocalDateTime.MIN),
            )

            for (updateRepositoryData in updateRepositoryDataList) {
                val result = updateRepositoryUseCase(
                    userIdentifier = someUserIdentifier,
                    repositoryName = someRepositoryName,
                    updateRepositoryData = updateRepositoryData,
                )

                result shouldBeRight Unit
                coVerify {
                    repositoryPersistence.updateRepository(
                        userIdentifier = someUserIdentifier,
                        repositoryName = someRepositoryName,
                        updateRepositoryData = updateRepositoryData,
                    )
                }
            }
        }
    }
}
