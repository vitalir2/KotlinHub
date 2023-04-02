package io.vitalir.kotlinhub.server.app.infrastructure.git

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryFile
import io.vitalir.kotlinhub.server.app.infrastructure.config.AppConfig
import io.vitalir.kotlinhub.server.app.infrastructure.logging.Logger
import io.vitalir.kotlinhub.shared.feature.user.UserId
import java.nio.file.Path
import org.eclipse.jgit.lib.FileMode
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.lib.RepositoryBuilder
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.treewalk.TreeWalk
import org.eclipse.jgit.treewalk.filter.PathFilter
import kotlin.io.path.Path
import kotlin.io.path.deleteIfExists
import org.eclipse.jgit.lib.Repository as JGitRepository

private const val ROOT_DIR_PATH = ""

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
    ): Either<GitError, List<RepositoryFile>> = withRepository(userId, repositoryName) {
        getDirContent(
            commit = headCommit,
            path = path,
        )
    }

    override fun getRepositoryFileContent(
        userId: UserId,
        repositoryName: String,
        path: String,
    ): Either<GitError, ByteArray> = withRepository(userId, repositoryName) {
        TreeWalk(this).use { walk ->
            walk.addTree(headCommit.tree)
            val directoryPath = if ('/' in path) path.substringBeforeLast('/') else ROOT_DIR_PATH
            if (walk.openDirByPath(directoryPath)) {
                getFileContentInCurrentDir(path, walk)
            } else {
                GitError.DirectoryNotFound(directoryPath).left()
            }
        }
    }

    private fun getFileContentInCurrentDir(
        path: String,
        walk: TreeWalk,
    ): Either<GitError.FileNotFound, ByteArray> {
        val fileName = path.substringAfterLast('/')
        walk.filter = PathFilter.create(fileName)
        walk.next()
        val fileId = walk.getObjectId(0)
        return if (fileId == ObjectId.zeroId()) {
            GitError.FileNotFound(path).left()
        } else {
            val loader = walk.objectReader.open(fileId)
            loader.cachedBytes.right()
        }
    }

    private val JGitRepository.headCommit: RevCommit
        get() = parseCommit(findRef("HEAD").objectId)

    private fun <T> withRepository(
        userId: UserId,
        repositoryName: String,
        action: JGitRepository.() -> T,
    ): T {
        val repositoryPath = createRepositoryPath(userId, repositoryName)
        return openRepository(repositoryPath).use(action)
    }

    private fun JGitRepository.getDirContent(
        commit: RevCommit,
        path: String,
    ): Either<GitError, List<RepositoryFile>> {
        val tree = commit.tree
        return TreeWalk(this).use { walk ->
            walk.addTree(tree)
            if (walk.openDirByPath(path)) {
                walk.getCurrentDirFiles().right()
            } else {
                GitError.DirectoryNotFound(path).left()
            }
        }
    }

    private fun TreeWalk.getCurrentDirFiles(): List<RepositoryFile> {
        return buildList {
            while (next()) {
                add(getCurrentRepositoryFile())
            }
        }
    }

    private fun TreeWalk.openDirByPath(
        rawTargetPath: String,
        rawInitPath: String = ROOT_DIR_PATH,
    ): Boolean {
        val targetPath = rawTargetPath.removeSurrounding("/")
        val initPath = rawInitPath.removeSurrounding("/")
        if (!canOpenDirFromTheCurrentOne(initPath, targetPath)) {
            return false
        }

        var currentPath = initPath
        while (currentPath != targetPath) {
            iterateToFirstFile()
            val nextDirPath = findAndIterateToNextFile(targetPath)
            if (nextDirPath != null && fileMode == FileMode.TREE) {
                currentPath = nextDirPath
                openCurrentTree()
            } else {
                return false
            }
        }

        return true
    }

    private fun TreeWalk.openCurrentTree() {
        val treeId = getObjectId(0)
        reset()
        addTree(treeId)
    }

    private fun TreeWalk.iterateToFirstFile() {
        next()
    }

    private fun canOpenDirFromTheCurrentOne(
        currentDir: String,
        targetDir: String,
    ): Boolean {
        val requiredPathSegmentsCount = currentDir.pathSegmentsCount
        val initPathSegmentsCount = targetDir.pathSegmentsCount
        return requiredPathSegmentsCount > initPathSegmentsCount
    }

    private fun TreeWalk.findAndIterateToNextFile(
        targetPath: String,
    ): String? {
        var currentPath: String
        var isCurrentFileOnTargetPath: Boolean
        do {
            currentPath = pathString
            isCurrentFileOnTargetPath = targetPath.startsWith(currentPath)
        } while (!isCurrentFileOnTargetPath && next())

        return if (isCurrentFileOnTargetPath) currentPath else null
    }

    private fun TreeWalk.getCurrentRepositoryFile(): RepositoryFile = RepositoryFile(
        name = nameString,
        type = when (val fileMode = fileMode) {
            FileMode.REGULAR_FILE -> RepositoryFile.Type.REGULAR
            FileMode.TREE -> RepositoryFile.Type.FOLDER
            else -> RepositoryFile.Type.UNKNOWN.also {
                logger.log("Unknown type of repository file=$fileMode")
            }
        },
    )

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

    private val String.pathSegmentsCount: Int
        get() = removeSurrounding("/").count { it == '/' } + 1
}
