package io.vitalir.kotlinhub.server.app.feature.user.routes.login

import arrow.core.Either
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.algorithms.Algorithm
import io.bkbn.kompendium.core.metadata.PostInfo
import io.bkbn.kompendium.core.plugin.NotarizedRoute
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.extensions.respondWith
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserCredentials
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserError
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.LoginUseCase
import io.vitalir.kotlinhub.server.app.feature.user.routes.common.usersTag
import io.vitalir.kotlinhub.server.app.feature.user.routes.getErrorResponseData
import io.vitalir.kotlinhub.server.app.infrastructure.auth.AuthenticationPayload
import io.vitalir.kotlinhub.server.app.infrastructure.config.AppConfig
import io.vitalir.kotlinhub.server.app.infrastructure.docs.badRequestResponse
import io.vitalir.kotlinhub.server.app.infrastructure.docs.kompendiumDocs
import io.vitalir.kotlinhub.server.app.infrastructure.docs.reqType
import io.vitalir.kotlinhub.server.app.infrastructure.docs.resType
import io.vitalir.kotlinhub.shared.feature.user.UserId
import java.util.*
import java.util.concurrent.TimeUnit

internal fun Route.authRoute(
    jwtConfig: AppConfig.Auth.Jwt,
    loginUseCase: LoginUseCase,
) {
    route("/auth") {
        kompendiumDocs {
            usersTag()
            loginDocs()
        }
        loginRoute(jwtConfig, loginUseCase)
    }
}

private fun Route.loginRoute(
    jwtConfig: AppConfig.Auth.Jwt,
    loginUseCase: LoginUseCase,
) {
    post {
        val loginRequest = call.receive<LoginRequest>()
        val usernameResult = loginUseCase(
            UserCredentials(
                identifier = UserIdentifier.Username(loginRequest.username),
                password = loginRequest.password,
            )
        )
        val responseData = getResponseData(jwtConfig, usernameResult)
        call.respondWith(responseData)
    }
}

private fun NotarizedRoute.Config.loginDocs() {
    post = PostInfo.builder {
        summary("Login to KotlinGit")
        description("Login using credentials")
        request {
            reqType<LoginRequest>()
            description("Credentials")
        }
        response {
            resType<LoginResponse>()
            responseCode(HttpStatusCode.OK)
            description("JWT token")
        }
        badRequestResponse()
    }
}

private fun getResponseData(
    jwtConfig: AppConfig.Auth.Jwt,
    loginResult: Either<UserError, User>,
): ResponseData {
    return when (loginResult) {
        is Either.Left -> {
            getErrorResponseData(loginResult.value)
        }

        is Either.Right -> {
            val user = loginResult.value
            val token = createToken(jwtConfig, user.id)
            ResponseData(
                code = HttpStatusCode.OK,
                body = LoginResponse(
                    token = token,
                ),
            )
        }
    }
}

private fun createToken(
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
