package io.vitalir.kotlinvcshub.server.infrastructure.git

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.util.*
import io.vitalir.kotlinvcshub.server.infrastructure.git.config.GitPluginConfig
import io.vitalir.kotlinvcshub.server.infrastructure.git.internal.GitActionRequest
import io.vitalir.kotlinvcshub.server.infrastructure.git.internal.GitApi
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.isDirectory

private const val PLUGIN_NAME = "KGitServer"

internal val GitPlugin = createApplicationPlugin(PLUGIN_NAME, ::GitPluginConfig) {
    val networkToFilePathConverter = pluginConfig.dependencies.networkToFilePathConverter
    val gitRepositoryActionCallMatcher = pluginConfig.dependencies.gitRepositoryActionCallMatcher
    val git = pluginConfig.git
    application.initGitRepositoriesDirectory(pluginConfig.baseRepositoriesPath)
    onCallReceive { call ->
        if (gitRepositoryActionCallMatcher.matches(call.request.uri)) {
            val networkPath = call.request.path()
            val repositoryRootPath = networkToFilePathConverter.convert(networkPath)
            git.handleAction(
                GitActionRequest(
                    repositoryRootPath = repositoryRootPath,
                    networkPath = networkPath,
                    queryParams = call.request.queryParameters
                        .toMap()
                        .mapValues { it.value.first() }
                    ,
                )
            )
        }
    }
}

private fun Application.initGitRepositoriesDirectory(rootStringPath: String) {
    val rootPath = Path(rootStringPath)
    if (!rootPath.isDirectory()) {
        log.info("Creating base git repos dir")
        rootPath.createDirectories()
    }
}

private suspend fun GitApi.handleAction(request: GitActionRequest) {
    // TODO parse action here, then move into separate class
}
