package io.vitalir.kotlinvcshub.server.infrastructure.git

import io.ktor.server.application.*
import io.ktor.server.application.hooks.*
import kotlin.io.path.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.isDirectory

internal val GitPlugin = createApplicationPlugin("kotlinHubGit") {
    on(MonitoringEvent(ApplicationStarted)) {
        val repositoriesPath = Path(GitConstants.REPOSITORIES_PATH)
        if (!repositoriesPath.isDirectory()) {
            repositoriesPath.createDirectory()
        }
    }
}
