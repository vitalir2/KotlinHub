package io.vitalir.kotlinhub.server.app.infrastructure.git

import io.vitalir.kotlinhub.server.app.infrastructure.config.AppConfig
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import java.io.File
import org.eclipse.jgit.lib.RepositoryBuilder
import kotlin.io.path.Path

internal class GitManagerImpl(
    private val repositoryConfig: AppConfig.Repository,
) : GitManager {

    override suspend fun initRepository(repository: Repository) {
        RepositoryBuilder().apply {
            gitDir = repository.toFilePath()
            setBare()
        }.build().use { fileSystemRepository ->
            fileSystemRepository.create(true)
        }
    }

    private fun Repository.toFilePath(): File {
        return Path(repositoryConfig.baseRepositoriesPath, owner.username, name).toFile()
    }
}
