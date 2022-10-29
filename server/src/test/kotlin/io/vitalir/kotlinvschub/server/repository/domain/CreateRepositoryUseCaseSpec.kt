package io.vitalir.kotlinvschub.server.repository.domain

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import io.vitalir.kotlinvcshub.server.repository.domain.CreateRepositoryData
import io.vitalir.kotlinvcshub.server.repository.domain.Repository
import io.vitalir.kotlinvcshub.server.repository.domain.RepositoryError
import io.vitalir.kotlinvcshub.server.repository.domain.RepositoryPersistence
import io.vitalir.kotlinvcshub.server.repository.domain.usecases.CreateRepositoryUseCase
import io.vitalir.kotlinvcshub.server.repository.domain.usecases.impl.CreateRepositoryUseCaseImpl
import io.vitalir.kotlinvcshub.server.user.domain.persistence.UserPersistence
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class CreateRepositoryUseCaseSpec : ShouldSpec() {

    private lateinit var createRepositoryUseCase: CreateRepositoryUseCase

    private lateinit var userPersistence: UserPersistence

    private lateinit var repositoryPersistence: RepositoryPersistence

    init {
        beforeEach {
            userPersistence = mockk()
            repositoryPersistence = spyk()
            createRepositoryUseCase = CreateRepositoryUseCaseImpl(
                userPersistence = userPersistence,
                repositoryPersistence = repositoryPersistence,
            )
        }

        should("return success if data is valid") {
            val userId = 123
            val repositoryName = "anyname"
            val repositoryAccessMode = Repository.AccessMode.PUBLIC
            val createRepositoryData = CreateRepositoryData(
                userId = userId,
                name = repositoryName,
                accessMode = repositoryAccessMode,
            )
            val createdAtDateTime = LocalDateTime.of(
                LocalDate.of(2022, 12, 12),
                LocalTime.of(12, 20, 5),
            )
            coEvery { userPersistence.isUserExists(userId) } returns true
            coEvery { repositoryPersistence.isRepositoryExists(userId, repositoryName) } returns false

            val result = createRepositoryUseCase(createRepositoryData)

            val expectedRepository = Repository(
                ownerId = createRepositoryData.userId,
                name = createRepositoryData.name,
                accessMode = createRepositoryData.accessMode,
                description = createRepositoryData.description,
                createdAt = createdAtDateTime,
                lastUpdated = createdAtDateTime,
            )
            result shouldBeRight Unit
            coVerify { repositoryPersistence.addRepository(expectedRepository) }
        }

        should("return error if user does not exist") {
            val notExistingUserId = 5
            coEvery { userPersistence.isUserExists(notExistingUserId) } returns false
            coEvery { repositoryPersistence.isRepositoryExists(notExistingUserId, any()) } returns false

            val result = createRepositoryUseCase(
                CreateRepositoryData(
                    userId = notExistingUserId,
                    name = "any",
                    accessMode = Repository.AccessMode.PUBLIC,
                )
            )

            result shouldBeLeft RepositoryError.Create.InvalidUserId
        }

        should("return error if repository with this name already exists") {
            val userId = 123
            val existingRepositoryName = "any"
            coEvery { userPersistence.isUserExists(userId) } returns true
            coEvery { repositoryPersistence.isRepositoryExists(userId, any()) } returns true

            val result = createRepositoryUseCase(
                CreateRepositoryData(
                    userId = userId,
                    name = existingRepositoryName,
                    accessMode = Repository.AccessMode.PUBLIC,
                )
            )

            result shouldBeLeft RepositoryError.Create.RepositoryAlreadyExists
        }
    }
}
