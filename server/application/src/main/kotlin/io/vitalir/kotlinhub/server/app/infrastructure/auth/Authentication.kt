package io.vitalir.kotlinhub.server.app.infrastructure.auth

import arrow.core.continuations.either
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.vitalir.kotlinhub.server.app.common.routes.AuthVariant
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.extensions.respondWith
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserCredentials
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserError
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.validation.UsernameValidationRule
import io.vitalir.kotlinhub.server.app.infrastructure.config.AppConfig
import io.vitalir.kotlinhub.server.app.infrastructure.di.AppGraph
import kotlinx.coroutines.launch

internal fun Application.configureAuth(
    appGraph: AppGraph,
) {
    val authConfig = appGraph.appConfig.auth
    jwtAuthentication(authConfig.jwt)
    launch { createDefaultAdmin(authConfig.defaultAdmin, appGraph) }
}

private fun Application.jwtAuthentication(jwtConfig: AppConfig.Auth.Jwt) {
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
                call.respondWith(responseData)
            }
        }
    }
}

private suspend fun createDefaultAdmin(
    config: AppConfig.Auth.DefaultAdmin,
    appGraph: AppGraph,
) {
    either {
        val username = UserIdentifier.Username(config.username)
        UsernameValidationRule.validate(username).bind()
        val admin = User.fromCredentials(
            credentials = UserCredentials(
                identifier = username,
                password = config.password,
            ),
            passwordManager = appGraph.auth.passwordManager,
        ).copy(isAdmin = true)
        appGraph.user.userPersistence.addUser(admin).bind()
    }
        .tapLeft { handleAdminCreatingError(it) }
}

private fun handleAdminCreatingError(error: UserError) {
    when (error) {
        UserError.ValidationFailed -> error("Cannot create default admin: credentials invalid")
        UserError.InvalidCredentials -> error("Impossible case")
        UserError.UserAlreadyExists -> { /* Ignore, it's ok */ }
    }
}

private fun AppConfig.Auth.Jwt.toJwtVerifier(): JWTVerifier {
    return JWT.require(Algorithm.HMAC256(secret)).apply {
        withAudience(audience)
        withIssuer(issuer)
    }.build()
}
