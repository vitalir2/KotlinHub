package io.vitalir.kotlinhub.server.app.infrastructure.security

import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configureSecurity() {
    install(CORS) {
        allowHost("localhost:8090")
    }
}
