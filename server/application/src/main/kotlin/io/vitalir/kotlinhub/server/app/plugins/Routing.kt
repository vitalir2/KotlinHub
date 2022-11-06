package io.vitalir.kotlinhub.server.app.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.feature.git.routes.gitRoutes
import io.vitalir.kotlinhub.server.app.infrastructure.di.AppGraph
import io.vitalir.kotlinhub.server.app.infrastructure.git.GitPlugin
import io.vitalir.kotlinhub.server.app.repository.routing.repositoryRoutes
import io.vitalir.kotlinhub.server.app.user.routes.userRoutes

fun Application.configureRouting(appGraph: AppGraph) {
    val debugConfig = appGraph.appConfig.debug

    install(CallLogging)
    install(GitPlugin)

    routing {
        if (debugConfig?.isRoutesTracingEnabled == true) {
            trace { application.log.trace(it.buildText()) }
        }

        userRoutes(
            jwtConfig = appGraph.appConfig.jwt,
            userGraph = appGraph.user,
        )
        repositoryRoutes(
            repositoryGraph = appGraph.repository,
        )
        gitRoutes()
    }
}