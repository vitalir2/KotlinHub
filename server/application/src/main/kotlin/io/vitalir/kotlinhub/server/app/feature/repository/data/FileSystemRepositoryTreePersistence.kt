package io.vitalir.kotlinhub.server.app.feature.repository.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryFile
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryIdentifier
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.error.RepositoryDoesNotExist
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.error.RepositoryError
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryTreePersistence
import io.vitalir.kotlinhub.server.app.infrastructure.git.GitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.io.path.isDirectory
import kotlin.io.path.name

internal class FileSystemRepositoryTreePersistence(
    private val gitManager: GitManager,
    private val identifierConverter: RepositoryIdentifierConverter,
) : RepositoryTreePersistence {

    override suspend fun getDirFiles(
        repositoryIdentifier: RepositoryIdentifier,
        absolutePath: String,
    ): Either<RepositoryError, List<RepositoryFile>> = withContext(Dispatchers.IO) {
        val (ownerIdentifier, repositoryName) = try {
            identifierConverter.getUserAndRepositoryName(repositoryIdentifier)
        } catch (exception: Exception) {
            return@withContext RepositoryDoesNotExist(repositoryIdentifier).left()
        }
        
        val files = gitManager.getRepositoryFiles(
            ownerIdentifier.value,
            repositoryName,
        )

        files.map { path ->
            RepositoryFile(
                name = path.name,
                type = when {
                    path.isDirectory() -> RepositoryFile.Type.FOLDER
                    else -> RepositoryFile.Type.SIMPLE
                },
            )
        }.right()
    }
}
