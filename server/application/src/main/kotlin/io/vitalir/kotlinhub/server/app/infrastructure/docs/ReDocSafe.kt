package io.vitalir.kotlinhub.server.app.infrastructure.docs

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.extensions.respondWith
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.infrastructure.auth.userId
import io.vitalir.kotlinhub.server.app.infrastructure.di.AppGraph
import kotlinx.html.body
import kotlinx.html.head
import kotlinx.html.link
import kotlinx.html.meta
import kotlinx.html.script
import kotlinx.html.style
import kotlinx.html.title
import kotlinx.html.unsafe

internal fun Route.safeRedoc(
    path: String,
    appGraph: AppGraph,
) {
    route(path) {
        get {
            val userId = call.userId
            val user = appGraph.user.getUserByIdentifierUseCase(UserIdentifier.Id(userId))
            if (user?.isAdmin == false) {
                call.respondWith(
                    ResponseData.fromErrorData(
                        code = HttpStatusCode.Forbidden,
                        errorMessage = "Forbidden",
                    )
                )
                return@get
            }

            call.respondReDoc()
        }
    }
}

private suspend fun ApplicationCall.respondReDoc() {
    respondHtml(HttpStatusCode.OK) {
        head {
            title {
                +"KotlinGit API"
            }
            meta {
                charset = "utf-8"
            }
            meta {
                name = "viewport"
                content = "width=device-width, initial-scale=1"
            }
            link {
                href = "https://fonts.googleapis.com/css?family=Montserrat:300,400,700|Roboto:300,400,700"
                rel = "stylesheet"
            }
            style {
                unsafe {
                    raw("body { margin: 0; padding: 0; }")
                }
            }
        }
        body {
            unsafe { +"<redoc spec-url='/openapi.json'></redoc>" }
            script {
                src = "https://cdn.jsdelivr.net/npm/redoc@next/bundles/redoc.standalone.js"
            }
        }
    }
}
