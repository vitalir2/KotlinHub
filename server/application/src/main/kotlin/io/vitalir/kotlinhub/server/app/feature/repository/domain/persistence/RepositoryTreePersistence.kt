package io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence

import arrow.core.Either
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryFile
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryIdentifier
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.error.RepositoryError

interface RepositoryTreePersistence {

    suspend fun getDirFiles(
        repositoryIdentifier: RepositoryIdentifier,
        absolutePath: String,
    ): Either<RepositoryError, List<RepositoryFile>>

    suspend fun getFileContent(
        repositoryIdentifier: RepositoryIdentifier,
        absolutePath: String,
    ): Either<RepositoryError, ByteArray>
}
