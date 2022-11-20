package io.vitalir.kotlinhub.server.app.infrastructure.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.extensions.respondWith
import io.vitalir.kotlinhub.server.app.feature.git.routes.gitRoutes
import io.vitalir.kotlinhub.server.app.infrastructure.di.AppGraph
import io.vitalir.kotlinhub.server.app.infrastructure.git.GitPlugin
import io.vitalir.kotlinhub.server.app.feature.repository.routes.repositoryRoutes
import io.vitalir.kotlinhub.server.app.feature.user.routes.userRoutes
import io.vitalir.kotlinhub.server.app.infrastructure.docs.openAPI

fun Application.configureRouting(appGraph: AppGraph) {
    val debugConfig = appGraph.appConfig.debug

    install(CallLogging)
    install(GitPlugin) {
        baseRepositoriesPath = appGraph.appConfig.repository.baseRepositoriesPath
    }
    install(IgnoreTrailingSlash)
    statusPages()

    routing {
        if (debugConfig?.isRoutesTracingEnabled == true) {
            trace { application.log.trace(it.buildText()) }
        }
        openAPI("/swagger")

        userRoutes(
            jwtConfig = appGraph.appConfig.jwt,
            userGraph = appGraph.user,
        )
        repositoryRoutes(
            repositoryGraph = appGraph.repository,
        )
        gitRoutes(
            appGraph = appGraph,
        )
    }
}

private fun Application.statusPages() {
    install(StatusPages) {
        exception<ServerException> { call, exception ->
            call.respondWith(
                ResponseData.fromErrorData(
                    code = HttpStatusCode.InternalServerError,
                    errorMessage = exception.message,
                )
            )
        }
    }
}
