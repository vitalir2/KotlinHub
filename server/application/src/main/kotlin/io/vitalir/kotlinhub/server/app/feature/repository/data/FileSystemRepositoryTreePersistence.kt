package io.vitalir.kotlinhub.server.app.feature.repository.data

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryFile
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryIdentifier
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.error.RepositoryDoesNotExist
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.error.RepositoryError
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.error.RepositoryFilePathDoesNotExist
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryTreePersistence
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.infrastructure.git.GitManager
import java.nio.file.NotDirectoryException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class FileSystemRepositoryTreePersistence(
    private val gitManager: GitManager,
    private val identifierConverter: RepositoryIdentifierConverter,
) : RepositoryTreePersistence {

    override suspend fun getDirFiles(
        repositoryIdentifier: RepositoryIdentifier,
        absolutePath: String,
    ): Either<RepositoryError, List<RepositoryFile>> = withContext(Dispatchers.IO) {
        either {
            val (ownerIdentifier, repositoryName) = getUserAndRepositoryNames(repositoryIdentifier).bind()
            Either.catch(
                fe = { throwable ->
                    when (throwable) {
                        is NotDirectoryException -> RepositoryFilePathDoesNotExist(absolutePath)
                        else -> throw throwable
                    }
                },
                f = {
                    gitManager.getRepositoryFiles(
                        userId = ownerIdentifier.value,
                        repositoryName = repositoryName,
                        path = absolutePath.removePrefix("/"),
                    )
                },
            ).bind()
        }
    }

    override suspend fun getFileContent(
        repositoryIdentifier: RepositoryIdentifier,
        absolutePath: String,
    ): Either<RepositoryError, ByteArray> = withContext(Dispatchers.IO) {
        either {
            val (ownerIdentifier, repositoryName) = getUserAndRepositoryNames(repositoryIdentifier).bind()
            gitManager.getRepositoryFileContent(
                userId = ownerIdentifier.value,
                repositoryName = repositoryName,
                path = absolutePath.removePrefix("/"),
            )
        }
    }

    private fun getUserAndRepositoryNames(
        repositoryIdentifier: RepositoryIdentifier
    ): Either<RepositoryError, Pair<UserIdentifier.Id, String>> {
        return try {
            identifierConverter.getUserAndRepositoryName(repositoryIdentifier).right()
        } catch (exception: Exception) {
            RepositoryDoesNotExist(repositoryIdentifier).left()
        }
    }
}
