package io.vitalir.kotlinvcshub.server.infrastructure.git

import io.vitalir.kotlinvcshub.server.repository.domain.model.Repository
import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvcshub.server.user.domain.persistence.UserPersistence
import java.nio.file.Path
import org.eclipse.jgit.lib.RepositoryBuilder
import kotlin.io.path.Path

internal class GitManagerImpl(
    private val userPersistence: UserPersistence,
) : GitManager {

    override suspend fun initRepository(repository: Repository) {
        val owner = userPersistence.getUser(repository.ownerId)!!
        RepositoryBuilder().apply {
            gitDir = repositoryPath(owner, repository).toFile()
            setBare()
        }.build().use { fileSystemRepository ->
            fileSystemRepository.create(true)
        }
    }

    private fun repositoryPath(repositoryOwner: User, repository: Repository): Path {
        return Path(GitConstants.REPOSITORIES_PATH, repositoryOwner.login, repository.name)
    }
}
