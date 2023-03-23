package io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl

import arrow.core.Either
import arrow.core.left
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryFile
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryIdentifier
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.error.RepositoryDoesNotExist
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.error.RepositoryError
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryTreePersistence
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.GetRepositoryDirFilesUseCase

internal class GetRepositoryDirFilesUseCaseImpl(
    private val repositoryPersistence: RepositoryPersistence,
    private val repositoryTreePersistence: RepositoryTreePersistence,
) : GetRepositoryDirFilesUseCase {

    override suspend fun invoke(
        repositoryIdentifier: RepositoryIdentifier,
        absolutePath: String,
    ): Either<RepositoryError, List<RepositoryFile>> {
        return when {
            !repositoryPersistence.isRepositoryExists(repositoryIdentifier) ->
                RepositoryDoesNotExist(repositoryIdentifier).left()
            else ->
                repositoryTreePersistence.getDirFiles(repositoryIdentifier, absolutePath)
        }
    }
}
