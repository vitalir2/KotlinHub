package io.vitalir.kotlinhub.server.app.infrastructure.git

import arrow.core.Either
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.shared.feature.user.UserId
import io.vitalir.kotlinhub.server.app.infrastructure.config.AppConfig
import java.nio.file.Path
import org.eclipse.jgit.lib.RepositoryBuilder
import kotlin.io.path.Path
import kotlin.io.path.deleteIfExists

internal class GitManagerImpl(
    private val repositoryConfig: AppConfig.Repository,
) : GitManager {

    override suspend fun initRepository(repository: Repository): Either<GitManager.Error, Unit> {
        fun Throwable.gitError(): GitManager.Error {
            return when (this) {
                is IllegalStateException -> {
                    GitManager.Error.RepositoryAlreadyExists(repository)
                }
                else -> {
                    GitManager.Error.Unknown
                }
            }
        }

        return Either.catch(Throwable::gitError) {
            RepositoryBuilder().apply {
                gitDir = repository.toFilePath().toFile()
                setBare()
            }.build().use { fileSystemRepository ->
                fileSystemRepository.create(true)
            }
        }
    }

    override suspend fun removeRepositoryByName(userId: UserId, repositoryName: String): Boolean {
        val repositoryPath = createRepositoryPath(userId, repositoryName)
        return repositoryPath.deleteIfExists()
    }

    private fun Repository.toFilePath(): Path {
        return createRepositoryPath(
            userId = owner.id,
            repositoryName = name,
        )
    }

    private fun createRepositoryPath(
        userId: UserId,
        repositoryName: String,
    ): Path {
        return Path(
            repositoryConfig.baseRepositoriesPath,
            userId.toString(),
            repositoryName,
        )
    }
}
