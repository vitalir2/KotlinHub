package io.vitalir.kotlinhub.server.app.feature.user.routes.common

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.algorithms.Algorithm
import io.vitalir.kotlinhub.server.app.infrastructure.auth.AuthenticationPayload
import io.vitalir.kotlinhub.server.app.infrastructure.config.AppConfig
import io.vitalir.kotlinhub.shared.feature.user.UserId
import java.util.*
import java.util.concurrent.TimeUnit

fun createToken(
    jwtConfig: AppConfig.Auth.Jwt,
    userId: UserId,
): String {
    return JWT.create().apply {
        withAudience(jwtConfig.audience)
        withIssuer(jwtConfig.issuer)
        withUserId(AuthenticationPayload.UserId(userId))
        withExpiresAt(Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1)))
    }.sign(Algorithm.HMAC256(jwtConfig.secret))
}

private fun JWTCreator.Builder.withUserId(payload: AuthenticationPayload.UserId) {
    withClaim(payload.name, payload.value)
}
