package io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase

import arrow.core.Either
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryFile
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryIdentifier
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.error.RepositoryError

interface GetRepositoryDirFilesUseCase {

    suspend operator fun invoke(
        repositoryIdentifier: RepositoryIdentifier,
        absolutePath: String,
    ): Either<RepositoryError, List<RepositoryFile>>
}
