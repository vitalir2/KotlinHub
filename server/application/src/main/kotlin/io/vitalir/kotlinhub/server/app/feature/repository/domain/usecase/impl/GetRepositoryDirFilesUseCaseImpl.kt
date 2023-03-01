package io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl

import arrow.core.Either
import arrow.core.left
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryFile
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryIdentifier
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.GetRepositoryDirFilesUseCase

class GetRepositoryDirFilesUseCaseImpl(
    private val repositoryPersistence: RepositoryPersistence,
) : GetRepositoryDirFilesUseCase {

    override suspend fun invoke(
        repositoryIdentifier: RepositoryIdentifier,
        absolutePath: String,
    ): Either<GetRepositoryDirFilesUseCase.Error, List<RepositoryFile>> {
        if (!repositoryPersistence.isRepositoryExists(repositoryIdentifier)) {
            return GetRepositoryDirFilesUseCase.Error.RepositoryDoesNotExist(repositoryIdentifier).left()
        }
        return GetRepositoryDirFilesUseCase.Error.RepositoryDirDoesNotExist(absolutePath).left()
    }
}
