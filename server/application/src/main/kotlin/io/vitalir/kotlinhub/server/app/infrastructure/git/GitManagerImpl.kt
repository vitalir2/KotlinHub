package io.vitalir.kotlinhub.server.app.infrastructure.git

import arrow.core.Either
import io.vitalir.kotlinhub.server.app.infrastructure.config.AppConfig
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import java.io.File
import org.eclipse.jgit.lib.RepositoryBuilder
import kotlin.io.path.Path

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
                gitDir = repository.toFilePath()
                setBare()
            }.build().use { fileSystemRepository ->
                fileSystemRepository.create(true)
            }
        }
    }

    private fun Repository.toFilePath(): File {
        return Path(
            repositoryConfig.baseRepositoriesPath,
            owner.id.toString(),
            name,
        ).toFile()
    }
}
