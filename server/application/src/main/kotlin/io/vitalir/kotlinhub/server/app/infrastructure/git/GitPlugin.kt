package io.vitalir.kotlinhub.server.app.infrastructure.git

import io.ktor.server.application.*
import io.ktor.server.application.hooks.*
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.isDirectory

internal val GitPlugin = createApplicationPlugin("kotlinHubGit", ::GitPluginConfig) {
    on(MonitoringEvent(ApplicationStarted)) { application ->
        val repositoriesPath = Path(pluginConfig.baseRepositoriesPath)
        if (!repositoriesPath.isDirectory()) {
            application.log.info("Creating base git repos dir")
            repositoriesPath.createDirectories()
        }
    }
}

internal class GitPluginConfig {

    var baseRepositoriesPath: String = ""
}
