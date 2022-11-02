package io.vitalir.kotlinvcshub.server.infrastructure.git

import io.ktor.server.application.*
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.isDirectory

internal class GitPluginConfig {

    var baseRepositoriesPath: String = GitConstants.REPOSITORIES_PATH
}

internal val GitPlugin = createApplicationPlugin("KGitServer", ::GitPluginConfig) {
    application.initGitDirectory()
}

private fun Application.initGitDirectory() {
    val repositoriesPath = Path(GitConstants.REPOSITORIES_PATH)
    if (!repositoriesPath.isDirectory()) {
        log.info("Creating base git repos dir")
        repositoriesPath.createDirectories()
    }
}
