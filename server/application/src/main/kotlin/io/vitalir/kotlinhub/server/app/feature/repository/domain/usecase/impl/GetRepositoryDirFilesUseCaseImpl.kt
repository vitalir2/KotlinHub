package io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl

import arrow.core.Either
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryFile
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryIdentifier
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.GetRepositoryDirFilesUseCase

class GetRepositoryDirFilesUseCaseImpl : GetRepositoryDirFilesUseCase {

    override suspend fun invoke(
        repositoryIdentifier: RepositoryIdentifier,
        absolutePath: String,
    ): Either<GetRepositoryDirFilesUseCase.Error, List<RepositoryFile>> {
        TODO("Not yet implemented")
    }
}
