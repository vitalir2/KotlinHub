package io.vitalir.kotlinvcshub.server.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.routing.*
import io.vitalir.kotlinvcshub.server.infrastructure.di.AppGraph
import io.vitalir.kotlinvcshub.server.repository.routing.repositoryRoutes
import io.vitalir.kotlinvcshub.server.user.routes.userRoutes

fun Application.configureRouting(appGraph: AppGraph) {
    install(CallLogging)
    routing {
        userRoutes(
            jwtConfig = appGraph.appConfig.jwt,
            userGraph = appGraph.user,
        )
        repositoryRoutes(
            repositoryGraph = appGraph.repository,
        )
    }
}
