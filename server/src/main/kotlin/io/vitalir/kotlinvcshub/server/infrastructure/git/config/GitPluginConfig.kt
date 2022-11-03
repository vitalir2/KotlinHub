package io.vitalir.kotlinvcshub.server.infrastructure.git.config

import io.vitalir.kotlinvcshub.server.infrastructure.git.GitConstants
import io.vitalir.kotlinvcshub.server.infrastructure.git.internal.GitApi
import io.vitalir.kotlinvcshub.server.infrastructure.git.internal.GitImpl

// TODO write doc for all things
internal class GitPluginConfig {

    // Expects URI like http://localhost:8080/repository/username/repositoryName.git?service=unpack_service
    private val defaultGitUriParser: GitUriParser by lazy {
        GitUriParser { uri ->
            val urlWithoutBase = uri.removePrefix(BASE_URL)
            val threeParts = urlWithoutBase.split("/")
            val isExactlyThreeParts = threeParts.size == 3
            if (!isExactlyThreeParts) return@GitUriParser null

            val hasRepositoryPrefix = REPOSITORY_PREFIX == threeParts.first()
            val isGitFile = uri.endsWith(GIT_FILE_EXTENSION)
            if (!hasRepositoryPrefix || !isGitFile) return@GitUriParser null

            val (repositoryName, queryParamsAsString) = threeParts[2].split("?")
            GitUriParser.Result(
                userLogin = threeParts[1],
                repositoryName = repositoryName.removeSuffix(GIT_FILE_EXTENSION),
                queryParams = queryParamsAsString
                    .split("&")
                    .associate { keyToParam -> keyToParam.partition { char -> char == '=' } },
            )
        }
    }

    private val defaultDependencies: Dependencies = object : Dependencies {

        override val gitUriParser: GitUriParser =
            defaultGitUriParser
    }

    val git: GitApi = GitImpl()

    var baseRepositoriesPath: String = GitConstants.REPOSITORIES_PATH

    var dependencies: Dependencies = defaultDependencies

    interface Dependencies {

        val gitUriParser: GitUriParser
    }

    companion object {
        private const val BASE_URL = "http://localhost:8080/"
        private const val GIT_FILE_EXTENSION = ".git"
        private const val REPOSITORY_PREFIX = "repository"
    }
}
