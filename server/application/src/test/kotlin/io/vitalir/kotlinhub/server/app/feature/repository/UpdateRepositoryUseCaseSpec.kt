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
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
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
        val someUser = User(
            id = someUserId,
            username = "someusername",
            password = "somepassword",
        )
        val someRepositoryName = "helloworld"
        val someRepository = Repository(
            id = 123,
            owner = someUser,
            name = someRepositoryName,
            accessMode = Repository.AccessMode.PUBLIC,
            createdAt = LocalDateTime.MIN,
            updatedAt = LocalDateTime.MIN,
        )
        val someUpdateRepositoryData = UpdateRepositoryData(accessMode = Repository.AccessMode.PRIVATE)

        should("return error if user does not exist") {
            val userId = 132
            val userIdentifier = UserIdentifier.Id(userId)
            coEvery {
                userPersistence.getUser(userIdentifier)
            } returns null

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
                userPersistence.getUser(someUserIdentifier)
            } returns someUser
            coEvery {
                repositoryPersistence.getRepository(someUserIdentifier, repositoryName)
            } returns null

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
                userPersistence.getUser(someUserIdentifier)
            } returns someUser
            coEvery {
                repositoryPersistence.getRepository(someUserIdentifier, someRepositoryName)
            } returns someRepository
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
