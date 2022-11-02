package io.vitalir.kotlinvcshub.server.infrastructure.git.config

import io.vitalir.kotlinvcshub.server.infrastructure.git.GitConstants
import io.vitalir.kotlinvcshub.server.infrastructure.git.internal.GitApi
import io.vitalir.kotlinvcshub.server.infrastructure.git.internal.GitImpl

// TODO write doc for all things
internal class GitPluginConfig {

    private val defaultDependencies: Dependencies = object : Dependencies {

        private val defaultNetworkToFilePathConverter: NetworkToFilePathConverter =
            NetworkToFilePathConverter { url ->
                url // TODO
            }

        private val defaultGitRepositoryActionCallMatcher: GitRepositoryActionCallMatcher =
            GitRepositoryActionCallMatcher {  url ->
                false // TODO
            }

        override val networkToFilePathConverter: NetworkToFilePathConverter =
            defaultNetworkToFilePathConverter

        override val gitRepositoryActionCallMatcher: GitRepositoryActionCallMatcher =
            defaultGitRepositoryActionCallMatcher
    }

    val git: GitApi = GitImpl()

    var baseRepositoriesPath: String = GitConstants.REPOSITORIES_PATH

    var dependencies: Dependencies = defaultDependencies

    interface Dependencies {

        val networkToFilePathConverter: NetworkToFilePathConverter

        val gitRepositoryActionCallMatcher: GitRepositoryActionCallMatcher
    }
}
