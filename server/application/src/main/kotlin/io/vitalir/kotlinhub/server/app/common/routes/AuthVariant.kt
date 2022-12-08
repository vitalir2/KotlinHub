package io.vitalir.kotlinhub.server.app.common.routes

import io.ktor.server.auth.*
import io.ktor.server.routing.*

enum class AuthVariant(val authName: String) {
    JWT("jwt-auth"),
}

internal fun Route.jwtAuth(
    optional: Boolean = false,
    block: Route.() -> Unit,
) {
    authenticate(AuthVariant.JWT.authName, optional = optional) {
        block()
    }
}
