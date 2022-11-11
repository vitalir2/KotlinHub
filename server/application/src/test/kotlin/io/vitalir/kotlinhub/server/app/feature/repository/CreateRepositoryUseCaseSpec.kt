package io.vitalir.kotlinhub.server.app.feature.repository

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
import io.vitalir.kotlinhub.server.app.common.domain.Uri
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.CreateRepositoryData
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryError
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.CreateRepositoryUseCase
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl.CreateRepositoryUseCaseImpl
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserId
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
        val repositoryOwner = User(
            id = someUserId,
            login = "validlogin",
            password = "validpassword",
        )
        val someRepositoryName = repositoryNameProvider.next()
        val someRepositoryAccessMode = repositoryAccessModeProvider.next()

        should("return success if data is valid") {
            val nowDateTime = dateTimeProvider.next()

            coEvery { userPersistence.isUserExists(someUserId) } returns true
            coEvery { userPersistence.getUser(someUserId) } returns repositoryOwner
            coEvery { repositoryPersistence.isRepositoryExists(someUserId, someRepositoryName) } returns false
            every { localDateTimeProvider.now() } returns nowDateTime

            val createRepositoryData = CreateRepositoryData(
                ownerId = someUserId,
                name = someRepositoryName,
                accessMode = someRepositoryAccessMode,
            )

            val result = createRepositoryUseCase(createRepositoryData)

            val expectedRepository = Repository(
                owner = repositoryOwner,
                name = createRepositoryData.name,
                accessMode = createRepositoryData.accessMode,
                description = createRepositoryData.description,
                createdAt = nowDateTime,
                updatedAt = nowDateTime,
            )
            val uri = result.shouldBeRight()
            uri.value shouldBe "git://${Uri.HOST}/${repositoryOwner.login}/$someRepositoryName.git"
            coVerify { repositoryPersistence.addRepository(expectedRepository) }
            coVerify { gitManager.initRepository(expectedRepository) }
        }

        should("return error if user does not exist") {
            val notExistingUserId = userIdProvider.next()
            coEvery { userPersistence.isUserExists(notExistingUserId) } returns false
            coEvery { repositoryPersistence.isRepositoryExists(notExistingUserId, someRepositoryName) } returns false

            val result = createRepositoryUseCase(
                CreateRepositoryData(
                    ownerId = notExistingUserId,
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
                    ownerId = someUserId,
                    name = someRepositoryName,
                    accessMode = repositoryAccessModeProvider.next(),
                )
            )

            result shouldBeLeft RepositoryError.Create.RepositoryAlreadyExists
        }
    }
}
