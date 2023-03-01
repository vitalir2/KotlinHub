package io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase

import arrow.core.Either
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryFile
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryIdentifier

interface GetRepositoryDirFilesUseCase {

    suspend operator fun invoke(
        repositoryIdentifier: RepositoryIdentifier,
        absolutePath: String,
    ): Either<Error, List<RepositoryFile>>

    sealed interface Error {

        object Unknown : Error

        class RepositoryDoesNotExist(repositoryIdentifier: RepositoryIdentifier) : Error
        class RepositoryDirDoesNotExist(absolutePath: String) : Error
    }
}
