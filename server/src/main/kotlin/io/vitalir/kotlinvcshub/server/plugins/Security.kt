package io.vitalir.kotlinvcshub.server.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.vitalir.kotlinvcshub.server.common.routes.AuthVariant
import io.vitalir.kotlinvcshub.server.common.routes.ResponseData
import io.vitalir.kotlinvcshub.server.infrastructure.auth.userId
import io.vitalir.kotlinvcshub.server.infrastructure.config.AppConfig

internal fun Application.configureSecurity(
    jwtConfig: AppConfig.Jwt,
) {
    authentication {
        jwt(AuthVariant.JWT.authName) {
            realm = jwtConfig.realm

            verifier(jwtConfig.toJwtVerifier())

            validate { credential ->
                val userId = credential.payload.userId
                if (userId != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }

            challenge { _, _ ->
                val responseData = ResponseData.fromErrorData(
                    code = HttpStatusCode.Unauthorized,
                    errorMessage = "token is not valid or has expired",
                )
                call.respond(responseData)
            }
        }
    }
}

private fun AppConfig.Jwt.toJwtVerifier(): JWTVerifier {
    return JWT.require(Algorithm.HMAC256(secret)).apply {
        withAudience(audience)
        withIssuer(issuer)
    }.build()
}
