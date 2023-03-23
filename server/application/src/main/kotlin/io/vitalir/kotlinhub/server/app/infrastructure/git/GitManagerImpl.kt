package io.vitalir.kotlinhub.server.app.infrastructure.git

import arrow.core.Either
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryFile
import io.vitalir.kotlinhub.server.app.infrastructure.config.AppConfig
import io.vitalir.kotlinhub.server.app.infrastructure.logging.Logger
import io.vitalir.kotlinhub.shared.feature.user.UserId
import java.nio.file.Path
import org.eclipse.jgit.lib.FileMode
import org.eclipse.jgit.lib.RepositoryBuilder
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.treewalk.TreeWalk
import org.eclipse.jgit.treewalk.filter.PathFilter
import kotlin.io.path.Path
import kotlin.io.path.deleteIfExists
import org.eclipse.jgit.lib.Repository as JGitRepository

internal class GitManagerImpl(
    private val repositoryConfig: AppConfig.Repository,
    private val logger: Logger,
) : GitManager {

    override suspend fun initRepository(repository: Repository): Either<GitManager.Error, Unit> {
        fun Throwable.gitError(): GitManager.Error {
            return when (this) {
                is IllegalStateException -> GitManager.Error.RepositoryAlreadyExists(repository)
                else -> GitManager.Error.Unknown
            }
        }

        return Either.catch(Throwable::gitError) {
            openRepository(
                gitDir = repository.toFilePath(),
                isBare = true,
            ).use { repo ->
                repo.create(true)
                repo.config.apply {
                    setBoolean("http", null, "uploadpack", true)
                    setBoolean("http", null, "receivepack", true)
                }.save()
            }
        }
    }

    override fun getRepositoryFiles(
        userId: UserId,
        repositoryName: String,
        path: String,
    ): List<RepositoryFile> {
        val repositoryPath = createRepositoryPath(userId, repositoryName)
        val preparedPath = path.removePrefix("/")
        val repository = openRepository(repositoryPath)
        val head = repository.findRef("HEAD")
        val headCommit = repository.parseCommit(head.objectId)
        return repository.parseDirectoryTree(
            commit = headCommit,
            path = preparedPath,
        )
    }

    override fun getRepositoryFileContent(userId: UserId, repositoryName: String, path: String): ByteArray {
        val repositoryPath = createRepositoryPath(userId, repositoryName)
        val preparedPath = path.removePrefix("/")
        val repository = openRepository(repositoryPath)
        val head = repository.findRef("HEAD")
        val headCommit = repository.parseCommit(head.objectId)
        return repository.run {
            val tree = headCommit.tree
            TreeWalk(this).use { walk ->
                walk.addTree(tree)
                walk.filter = PathFilter.create(preparedPath)
                walk.next()
                val fileId = walk.getObjectId(0)
                val loader = walk.objectReader.open(fileId)
                loader.cachedBytes
            }
        }
    }

    private fun JGitRepository.parseDirectoryTree(commit: RevCommit, path: String): List<RepositoryFile> {
        val tree = commit.tree
        return TreeWalk(this).use { walk ->
            val result = mutableListOf<RepositoryFile>()
            walk.addTree(tree)
            if (path.isNotEmpty()) {
                walk.filter = PathFilter.create(path)
            }
            while (walk.next()) {
                val file = RepositoryFile(
                    name = walk.nameString,
                    type = when (walk.fileMode) {
                        FileMode.REGULAR_FILE -> RepositoryFile.Type.REGULAR
                        FileMode.TREE -> RepositoryFile.Type.FOLDER
                        else -> RepositoryFile.Type.UNKNOWN.also {
                            logger.log("Unknown type of repository file=${walk.fileMode}")
                        }
                    },
                )
                result.add(file)
            }
            result
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

    private fun openRepository(
        gitDir: Path,
        isBare: Boolean = false,
    ): JGitRepository {
        return RepositoryBuilder().apply {
            this.gitDir = gitDir.toFile()
            if (isBare) {
                setBare()
            }
        }.build()
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
