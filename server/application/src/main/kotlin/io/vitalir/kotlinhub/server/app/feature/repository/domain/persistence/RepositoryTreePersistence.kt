package io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence

import arrow.core.Either
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryFile
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryIdentifier
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.error.RepositoryFilePathDoesNotExist

interface RepositoryTreePersistence {

    suspend fun getDirFiles(
        repositoryIdentifier: RepositoryIdentifier,
        absolutePath: String,
    ): Either<RepositoryFilePathDoesNotExist, List<RepositoryFile>>
}
