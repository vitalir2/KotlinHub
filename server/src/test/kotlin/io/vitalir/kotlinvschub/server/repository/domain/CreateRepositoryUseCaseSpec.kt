package io.vitalir.kotlinvschub.server.repository.domain

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.enum
import io.kotest.property.arbitrary.localDateTime
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.arbitrary.string
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.vitalir.kotlinvcshub.server.common.domain.LocalDateTimeProvider
import io.vitalir.kotlinvcshub.server.repository.domain.model.CreateRepositoryData
import io.vitalir.kotlinvcshub.server.repository.domain.model.Repository
import io.vitalir.kotlinvcshub.server.repository.domain.model.RepositoryError
import io.vitalir.kotlinvcshub.server.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinvcshub.server.repository.domain.usecase.CreateRepositoryUseCase
import io.vitalir.kotlinvcshub.server.repository.domain.usecase.impl.CreateRepositoryUseCaseImpl
import io.vitalir.kotlinvcshub.server.user.domain.model.UserId
import io.vitalir.kotlinvcshub.server.user.domain.persistence.UserPersistence
import java.time.LocalDateTime

internal class CreateRepositoryUseCaseSpec : ShouldSpec() {

    private lateinit var createRepositoryUseCase: CreateRepositoryUseCase

    private lateinit var userPersistence: UserPersistence

    private lateinit var repositoryPersistence: RepositoryPersistence

    private lateinit var localDateTimeProvider: LocalDateTimeProvider

    private val userIdProvider: Arb<UserId> by lazy {
        Arb.positiveInt()
    }

    private val repositoryNameProvider: Arb<String> by lazy {
        Arb.string(6..50)
    }

    private val repositoryAccessModeProvider: Arb<Repository.AccessMode> by lazy {
        Arb.enum()
    }

    private val dateTimeProvider: Arb<LocalDateTime> by lazy {
        Arb.localDateTime(minYear = 2021, maxYear = 2022)
    }

    init {
        beforeEach {
            userPersistence = mockk()
            repositoryPersistence = spyk()
            localDateTimeProvider = mockk()
            createRepositoryUseCase = CreateRepositoryUseCaseImpl(
                userPersistence = userPersistence,
                repositoryPersistence = repositoryPersistence,
                localDateTimeProvider = localDateTimeProvider,
            )
        }

        should("return success if data is valid") {
            val userId = userIdProvider.next()
            val repositoryName = repositoryNameProvider.next()
            val repositoryAccessMode = repositoryAccessModeProvider.next()
            val createdAtDateTime = dateTimeProvider.next()

            coEvery { userPersistence.isUserExists(userId) } returns true
            coEvery { repositoryPersistence.isRepositoryExists(userId, repositoryName) } returns false
            every { localDateTimeProvider.now() } returns createdAtDateTime

            val createRepositoryData = CreateRepositoryData(
                userId = userId,
                name = repositoryName,
                accessMode = repositoryAccessMode,
            )

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
