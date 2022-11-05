package io.vitalir.kotlinhub.server.app.feature.git.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Routing.gitRoutes() {
    route("git/") {
        httpBaseAuth()
    }
}

// TODO
private fun Route.httpBaseAuth() {
    get("http/auth/") {
        call.respond(HttpStatusCode.OK)
    }
}
