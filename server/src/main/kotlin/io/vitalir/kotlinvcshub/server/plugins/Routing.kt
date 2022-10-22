package io.vitalir.kotlinvcshub.server.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.routing.*
import io.ktor.server.webjars.*
import io.vitalir.kotlinvcshub.server.infrastructure.di.AppGraph
import io.vitalir.kotlinvcshub.server.user.routes.userRoutes

fun Application.configureRouting(appGraph: AppGraph) {

    install(StatusPages) {

    }
    install(Webjars) {
        path = "/webjars" //defaults to /webjars
    }
    routing {
        userRoutes(
            jwtConfig = appGraph.appConfig.jwt,
            userGraph = appGraph.user,
        )
    }
}
