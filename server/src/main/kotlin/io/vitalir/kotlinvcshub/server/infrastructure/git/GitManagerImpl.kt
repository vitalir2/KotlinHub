package io.vitalir.kotlinvcshub.server.infrastructure.git

import io.vitalir.kotlinvcshub.server.repository.domain.model.Repository
import java.io.File
import org.eclipse.jgit.lib.RepositoryBuilder
import kotlin.io.path.Path

internal class GitManagerImpl : GitManager {

    override suspend fun initRepository(repository: Repository) {
        RepositoryBuilder().apply {
            gitDir = repository.toFilePath()
            setBare()
        }.build().use { fileSystemRepository ->
            fileSystemRepository.create(true)
        }
    }

    private fun Repository.toFilePath(): File {
        return Path(GitConstants.REPOSITORIES_PATH, owner.login, name).toFile()
    }
}
