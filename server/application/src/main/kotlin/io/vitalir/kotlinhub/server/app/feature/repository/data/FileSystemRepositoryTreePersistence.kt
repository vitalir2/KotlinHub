package io.vitalir.kotlinhub.server.app.feature.repository.data

import arrow.core.Either
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryFile
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.error.RepositoryFilePathDoesNotExist
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryTreePersistence

internal class FileSystemRepositoryTreePersistence : RepositoryTreePersistence {

    override suspend fun getDirFiles(absolutePath: String): Either<RepositoryFilePathDoesNotExist, List<RepositoryFile>> {
        TODO("Not yet implemented")
    }
}
