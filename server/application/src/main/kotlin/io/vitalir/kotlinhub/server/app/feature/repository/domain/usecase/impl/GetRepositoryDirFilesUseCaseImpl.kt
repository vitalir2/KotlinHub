package io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryFile
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryIdentifier
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
    ): Either<GetRepositoryDirFilesUseCase.Error, List<RepositoryFile>> {
        return when {
            !repositoryPersistence.isRepositoryExists(repositoryIdentifier) -> {
                GetRepositoryDirFilesUseCase.Error.RepositoryDoesNotExist(repositoryIdentifier).left()
            }
            else -> {
                when (val result = repositoryTreePersistence.getDirFiles(repositoryIdentifier, absolutePath)) {
                    is Either.Left ->
                        GetRepositoryDirFilesUseCase.Error.RepositoryDirDoesNotExist(result.value.absolutePath).left()
                    is Either.Right ->
                        result.value.right()
                }
            }
        }
    }
}
