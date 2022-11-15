package io.vitalir.kotlinhub.server.app.infrastructure.auth

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.*
import io.vitalir.kotlinhub.shared.feature.user.UserId
import io.vitalir.kotlinhub.server.app.infrastructure.routing.ServerException

internal val ApplicationCall.userId: UserId
    get() = principal<JWTPrincipal>()?.payload?.userId
        ?: throw ServerException("Cannot get userId from unauthorized route")

internal inline fun <T> ApplicationCall.requireParameter(name: String, converter: (String) -> T): T {
    val param = parameters[name]
        ?: throw BadRequestException("Parameter with name=$name isn't found")
    return converter(param)
}

internal fun ApplicationCall.requireParameter(name: String): String {
    return requireParameter(name) { it }
}
