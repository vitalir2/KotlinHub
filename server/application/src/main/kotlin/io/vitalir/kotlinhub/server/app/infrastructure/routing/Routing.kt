package io.vitalir.kotlinhub.server.app.infrastructure.routing

import io.bkbn.kompendium.core.routes.redoc
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.extensions.respondWith
import io.vitalir.kotlinhub.server.app.common.routes.jwtAuth
import io.vitalir.kotlinhub.server.app.feature.git.routes.gitRoutes
import io.vitalir.kotlinhub.server.app.feature.repository.routes.repositoryRoutes
import io.vitalir.kotlinhub.server.app.feature.user.routes.userRoutes
import io.vitalir.kotlinhub.server.app.infrastructure.di.AppGraph
import io.vitalir.kotlinhub.server.app.infrastructure.docs.safeRedoc
import io.vitalir.kotlinhub.server.app.infrastructure.git.GitPlugin

fun Application.configureRouting(appGraph: AppGraph) {
    install(CallLogging)
    install(GitPlugin) {
        baseRepositoriesPath = appGraph.appConfig.repository.baseRepositoriesPath
    }
    install(IgnoreTrailingSlash)
    statusPages()
    routes(appGraph)
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

private fun Application.routes(appGraph: AppGraph) {
    routing {
        val debugConfig = appGraph.appConfig.debug
        if (debugConfig?.isRoutesTracingEnabled == true) {
            trace { application.log.trace(it.buildText()) }
        }

        baseRoute {
            userRoutes(
                jwtConfig = appGraph.appConfig.auth.jwt,
                userGraph = appGraph.user,
            )
            repositoryRoutes(
                repositoryGraph = appGraph.repository,
            )
            gitRoutes(
                appGraph = appGraph,
            )

            val docsPath = "/docs"
            if (debugConfig?.isDocsEnabled == true) {
                // Order is important here
                redoc(path = docsPath)
            } else {
                jwtAuth {
                    safeRedoc(path = docsPath, appGraph)
                }
            }
        }
    }
}

private fun Route.baseRoute(block: Route.() -> Unit) {
    route("/api/v1") {
        block()
    }
}
