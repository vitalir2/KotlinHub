package io.vitalir.kotlinhub.server.app.feature.repository

import arrow.core.left
import arrow.core.right
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
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
import io.vitalir.kotlinhub.server.app.common.domain.LocalDateTimeProvider
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.CreateRepositoryData
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryError
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.CreateRepositoryUseCase
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl.CreateRepositoryUseCaseImpl
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserId
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence
import io.vitalir.kotlinhub.server.app.infrastructure.git.GitManager
import java.time.LocalDateTime

internal class CreateRepositoryUseCaseSpec : ShouldSpec() {

    private lateinit var createRepositoryUseCase: CreateRepositoryUseCase

    private lateinit var userPersistence: UserPersistence

    private lateinit var repositoryPersistence: RepositoryPersistence

    private lateinit var localDateTimeProvider: LocalDateTimeProvider

    private lateinit var gitManager: GitManager

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
            gitManager = spyk()
            createRepositoryUseCase = CreateRepositoryUseCaseImpl(
                userPersistence = userPersistence,
                repositoryPersistence = repositoryPersistence,
                localDateTimeProvider = localDateTimeProvider,
                gitManager = gitManager,
            )
        }

        val someUserId = 123
        val someUserIdIdentifier = UserIdentifier.Id(someUserId)
        val repositoryOwner = User(
            id = someUserId,
            username = "validlogin",
            password = "validpassword",
        )
        val someRepositoryId = 12
        val someRepositoryName = repositoryNameProvider.next()
        val someRepositoryAccessMode = repositoryAccessModeProvider.next()
        val nowDateTime = dateTimeProvider.next()
        val someRepository = Repository(
            id = someRepositoryId,
            owner = repositoryOwner,
            name = someRepositoryName,
            accessMode = someRepositoryAccessMode,
            createdAt = nowDateTime,
            updatedAt = nowDateTime,
        )

        should("return success if data is valid") {
            coEvery { userPersistence.isUserExists(someUserIdIdentifier) } returns true
            coEvery { userPersistence.getUser(someUserIdIdentifier) } returns repositoryOwner
            coEvery { repositoryPersistence.isRepositoryExists(someUserId, someRepositoryName) } returns false
            coEvery {
                gitManager.initRepository(any())
            } returns Unit.right()
            coEvery {
                repositoryPersistence.addRepository(any())
            } returns someRepositoryId
            every { localDateTimeProvider.now() } returns nowDateTime

            val createRepositoryData = CreateRepositoryData(
                ownerId = someUserId,
                name = someRepositoryName,
                accessMode = someRepositoryAccessMode,
            )

            val result = createRepositoryUseCase(createRepositoryData)

            val expectedRepository = Repository(
                id = someRepositoryId,
                owner = repositoryOwner,
                name = createRepositoryData.name,
                accessMode = createRepositoryData.accessMode,
                description = createRepositoryData.description,
                createdAt = nowDateTime,
                updatedAt = nowDateTime,
            )
            val url = result.shouldBeRight()
            url.toString() shouldBe "http://localhost/${repositoryOwner.username}/$someRepositoryName.git"
            coVerify {
                repositoryPersistence.addRepository(expectedRepository.copy(id = Repository.AUTOINCREMENT_ID))
            }
            coVerify {
                gitManager.initRepository(expectedRepository)
            }
        }

        should("return error if user does not exist") {
            val notExistingUserId = userIdProvider.next()
            coEvery { userPersistence.isUserExists(UserIdentifier.Id(notExistingUserId)) } returns false
            coEvery { repositoryPersistence.isRepositoryExists(notExistingUserId, someRepositoryName) } returns false

            val result = createRepositoryUseCase(
                CreateRepositoryData(
                    ownerId = notExistingUserId,
                    name = someRepositoryName,
                    accessMode = someRepositoryAccessMode,
                )
            )

            result shouldBeLeft RepositoryError.Create.UserDoesNotExist(UserIdentifier.Id(notExistingUserId))
        }

        should("return error if repository with this name already exists") {
            coEvery { userPersistence.isUserExists(UserIdentifier.Id(someUserId)) } returns true
            coEvery { repositoryPersistence.isRepositoryExists(someUserId, someRepositoryName) } returns true

            val result = createRepositoryUseCase(
                CreateRepositoryData(
                    ownerId = someUserId,
                    name = someRepositoryName,
                    accessMode = repositoryAccessModeProvider.next(),
                )
            )

            result shouldBeLeft RepositoryError.Create.RepositoryAlreadyExists(someUserId, someRepositoryName)
        }

        should("return error if repository already exists in file system") {
            coEvery {
                userPersistence.isUserExists(someUserIdIdentifier)
            } returns true
            coEvery {
                repositoryPersistence.isRepositoryExists(someUserId, someRepositoryName)
            } returns false
            coEvery {
                userPersistence.getUser(someUserIdIdentifier)
            } returns repositoryOwner
            coEvery {
                repositoryPersistence.addRepository(any())
            } returns someRepositoryId
            coEvery {
                gitManager.initRepository(any())
            } returns GitManager.Error.RepositoryAlreadyExists(someRepository).left()
            coEvery {
                localDateTimeProvider.now()
            } returns nowDateTime

            val result = createRepositoryUseCase(
                CreateRepositoryData(
                    ownerId = someRepository.owner.id,
                    name = someRepository.name,
                    accessMode = someRepository.accessMode,
                )
            )

            result shouldBeLeft RepositoryError.Create.RepositoryAlreadyExists(
                someRepository.owner.id,
                someRepository.name,
            )
        }

        should("return error if unexpected error happened") {
            coEvery {
                userPersistence.isUserExists(someUserIdIdentifier)
            } returns true
            coEvery {
                repositoryPersistence.isRepositoryExists(someUserId, someRepositoryName)
            } returns false
            coEvery {
                userPersistence.getUser(someUserIdIdentifier)
            } returns repositoryOwner
            coEvery {
                repositoryPersistence.addRepository(any())
            } returns someRepositoryId
            coEvery {
                gitManager.initRepository(any())
            } returns GitManager.Error.Unknown.left()
            coEvery {
                localDateTimeProvider.now()
            } returns nowDateTime

            val result = createRepositoryUseCase(
                CreateRepositoryData(
                    ownerId = someRepository.owner.id,
                    name = someRepository.name,
                    accessMode = someRepository.accessMode,
                )
            )

            result shouldBeLeft RepositoryError.Create.Unknown
        }
    }
}
