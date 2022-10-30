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

        val someUserId = 123
        val someRepositoryName = repositoryNameProvider.next()
        val someRepositoryAccessMode = repositoryAccessModeProvider.next()

        should("return success if data is valid") {
            val createdAtDateTime = dateTimeProvider.next()

            coEvery { userPersistence.isUserExists(someUserId) } returns true
            coEvery { repositoryPersistence.isRepositoryExists(someUserId, someRepositoryName) } returns false
            every { localDateTimeProvider.now() } returns createdAtDateTime

            val createRepositoryData = CreateRepositoryData(
                userId = someUserId,
                name = someRepositoryName,
                accessMode = someRepositoryAccessMode,
            )

            val result = createRepositoryUseCase(createRepositoryData)

            val expectedRepository = Repository(
                ownerId = createRepositoryData.userId,
                name = createRepositoryData.name,
                accessMode = createRepositoryData.accessMode,
                description = createRepositoryData.description,
                createdAt = createdAtDateTime,
                updatedAt = createdAtDateTime,
            )
            result shouldBeRight Unit
            coVerify { repositoryPersistence.addRepository(expectedRepository) }
        }

        should("return error if user does not exist") {
            val notExistingUserId = userIdProvider.next()
            coEvery { userPersistence.isUserExists(notExistingUserId) } returns false
            coEvery { repositoryPersistence.isRepositoryExists(notExistingUserId, someRepositoryName) } returns false

            val result = createRepositoryUseCase(
                CreateRepositoryData(
                    userId = notExistingUserId,
                    name = someRepositoryName,
                    accessMode = someRepositoryAccessMode,
                )
            )

            result shouldBeLeft RepositoryError.Create.InvalidUserId
        }

        should("return error if repository with this name already exists") {
            coEvery { userPersistence.isUserExists(someUserId) } returns true
            coEvery { repositoryPersistence.isRepositoryExists(someUserId, someRepositoryName) } returns true

            val result = createRepositoryUseCase(
                CreateRepositoryData(
                    userId = someUserId,
                    name = someRepositoryName,
                    accessMode = repositoryAccessModeProvider.next(),
                )
            )

            result shouldBeLeft RepositoryError.Create.RepositoryAlreadyExists
        }
    }
}
