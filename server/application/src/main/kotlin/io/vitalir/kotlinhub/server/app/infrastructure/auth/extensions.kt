package io.vitalir.kotlinhub.server.app.infrastructure.auth

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserId
import io.vitalir.kotlinhub.server.app.infrastructure.routing.ServerException

internal val ApplicationCall.userId: UserId
    get() = principal<JWTPrincipal>()?.payload?.userId
        ?: throw ServerException("Cannot get userId from unauthorized route")
