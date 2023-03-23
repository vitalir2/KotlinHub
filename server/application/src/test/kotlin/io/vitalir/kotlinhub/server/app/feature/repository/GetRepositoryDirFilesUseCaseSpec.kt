package io.vitalir.kotlinhub.server.app.feature.repository

import arrow.core.left
import arrow.core.right
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.coEvery
import io.mockk.mockk
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryFile
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryIdentifier
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.error.RepositoryDoesNotExist
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.error.RepositoryFilePathDoesNotExist
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryTreePersistence
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.GetRepositoryDirFilesUseCase
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl.GetRepositoryDirFilesUseCaseImpl

class GetRepositoryDirFilesUseCaseSpec : ShouldSpec() {

    private lateinit var repositoryPersistence: RepositoryPersistence

    private lateinit var repositoryTreePersistence: RepositoryTreePersistence

    private lateinit var useCase: GetRepositoryDirFilesUseCase

    init {
        beforeEach {
            repositoryPersistence = mockk()
            repositoryTreePersistence = mockk()
            useCase = GetRepositoryDirFilesUseCaseImpl(
                repositoryPersistence = repositoryPersistence,
                repositoryTreePersistence = repositoryTreePersistence,
            )
        }

        should("return error if repository does not exist") {
            val repositoryIdentifier = RepositoryIdentifier.Id(123)

            repositoryDoesNotExist(repositoryIdentifier)

            val result = useCase(
                repositoryIdentifier = repositoryIdentifier,
                absolutePath = "/",
            )

            result shouldBeLeft RepositoryDoesNotExist(repositoryIdentifier)
        }

        should("return error if path does not exist") {
            val repositoryIdentifier = RepositoryIdentifier.Id(123)
            val notExistingPath = "/kek"

            repositoryExists(repositoryIdentifier)
            repositoryPathDoesNotExist(repositoryIdentifier, notExistingPath)

            val result = useCase(
                repositoryIdentifier = repositoryIdentifier,
                absolutePath = notExistingPath,
            )

            result shouldBeLeft RepositoryFilePathDoesNotExist(notExistingPath)
        }

        should("return files for directory") {
            val repositoryIdentifier = RepositoryIdentifier.Id(123)
            val path = "/"
            val files = listOf(
                RepositoryFile("first", RepositoryFile.Type.REGULAR),
                RepositoryFile("folder", RepositoryFile.Type.FOLDER),
            )

            repositoryExists(repositoryIdentifier)
            repositoryPathExists(
                repositoryIdentifier,
                path,
                files,
            )

            val result = useCase(
                repositoryIdentifier = repositoryIdentifier,
                absolutePath = path,
            )

            result shouldBeRight files
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

    private fun repositoryPathExists(
        repositoryIdentifier: RepositoryIdentifier,
        path: String,
        filesOnPath: List<RepositoryFile>,
    ) {
        coEvery {
            repositoryTreePersistence.getDirFiles(repositoryIdentifier, path)
        } returns filesOnPath.right()
    }

    private fun repositoryPathDoesNotExist(
        repositoryIdentifier: RepositoryIdentifier,
        path: String,
    ) {
        coEvery {
            repositoryTreePersistence.getDirFiles(repositoryIdentifier, path)
        } returns RepositoryFilePathDoesNotExist(path).left()
    }
}
