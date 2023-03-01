package io.vitalir.kotlinhub.server.app.feature.repository

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.coEvery
import io.mockk.mockk
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryIdentifier
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.GetRepositoryDirFilesUseCase
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl.GetRepositoryDirFilesUseCaseImpl

class GetRepositoryDirFilesUseCaseSpec : ShouldSpec() {

    private lateinit var repositoryPersistence: RepositoryPersistence

    private lateinit var useCase: GetRepositoryDirFilesUseCase

    init {
        beforeEach {
            repositoryPersistence = mockk()
            useCase = GetRepositoryDirFilesUseCaseImpl(
                repositoryPersistence = repositoryPersistence,
            )
        }

        should("return error if repository does not exist") {
            val repositoryIdentifier = RepositoryIdentifier.Id(123)
            repositoryDoesNotExist(repositoryIdentifier)

            val result = useCase(
                repositoryIdentifier = repositoryIdentifier,
                absolutePath = "/",
            )

            result shouldBeLeft GetRepositoryDirFilesUseCase.Error.RepositoryDoesNotExist(repositoryIdentifier)
        }

        should("return error if path does not exist") {
            val repositoryIdentifier = RepositoryIdentifier.Id(123)
            val notExistingPath = "/kek"
            repositoryExists(repositoryIdentifier)

            val result = useCase(
                repositoryIdentifier = repositoryIdentifier,
                absolutePath = notExistingPath,
            )

            result shouldBeLeft GetRepositoryDirFilesUseCase.Error.RepositoryDirDoesNotExist(notExistingPath)
        }
    }

    private fun repositoryExists(repositoryIdentifier: RepositoryIdentifier) {
        coEvery {
            repositoryPersistence.isRepositoryExists(repositoryIdentifier)
        } returns true
    }

    private fun repositoryDoesNotExist(repositoryIdentifier: RepositoryIdentifier) {
        coEvery {
            repositoryPersistence.isRepositoryExists(repositoryIdentifier)
        } returns false
    }
}
