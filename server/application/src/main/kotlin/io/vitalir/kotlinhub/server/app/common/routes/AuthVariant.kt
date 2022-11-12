package io.vitalir.kotlinhub.server.app.common.routes

import io.ktor.server.auth.*
import io.ktor.server.routing.*

enum class AuthVariant(val authName: String) {
    JWT("jwt-auth"),
}

internal fun Route.jwtAuth(block: Route.() -> Unit) {
    authenticate(AuthVariant.JWT.authName) {
        block()
    }
}
