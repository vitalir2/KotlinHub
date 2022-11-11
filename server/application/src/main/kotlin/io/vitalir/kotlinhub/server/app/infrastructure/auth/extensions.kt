package io.vitalir.kotlinhub.server.app.infrastructure.auth

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserId


// TODO throw exception if null and catch it in status pages plugin
internal val ApplicationCall.userId: UserId?
    get() = principal<JWTPrincipal>()?.payload?.userId
