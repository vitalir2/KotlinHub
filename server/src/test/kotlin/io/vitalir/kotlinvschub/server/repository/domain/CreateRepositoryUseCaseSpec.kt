package io.vitalir.kotlinvschub.server.repository.domain

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.coEvery
import io.mockk.mockk
import io.vitalir.kotlinvcshub.server.repository.domain.CreateRepositoryData
import io.vitalir.kotlinvcshub.server.repository.domain.Repository
import io.vitalir.kotlinvcshub.server.repository.domain.RepositoryError
import io.vitalir.kotlinvcshub.server.repository.domain.usecases.CreateRepositoryUseCase
import io.vitalir.kotlinvcshub.server.repository.domain.usecases.impl.CreateRepositoryUseCaseImpl
import io.vitalir.kotlinvcshub.server.user.domain.persistence.UserPersistence

class CreateRepositoryUseCaseSpec : ShouldSpec() {

    private lateinit var createRepositoryUseCase: CreateRepositoryUseCase

    private lateinit var userPersistence: UserPersistence

    init {
        beforeEach {
            userPersistence = mockk()
            createRepositoryUseCase = CreateRepositoryUseCaseImpl(
                userPersistence = userPersistence,
            )
        }

        should("return success if data is valid") {
            val userId = 123
            coEvery { userPersistence.isUserExists(userId) } returns true

            val result = createRepositoryUseCase(
                CreateRepositoryData(
                    userId = userId,
                    name = "any",
                    accessMode = Repository.AccessMode.PUBLIC,
                )
            )

            result shouldBeRight Unit
        }

        should("return error if user does not exist") {
            val notExistingUserId = 5
            coEvery { userPersistence.isUserExists(notExistingUserId) } returns false

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
