package io.vitalir.kotlinhub.server.app.feature.user.routes.login

import arrow.core.Either
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.extensions.respondWith
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserCredentials
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserError
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserId
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.LoginUseCase
import io.vitalir.kotlinhub.server.app.feature.user.routes.getErrorResponseData
import io.vitalir.kotlinhub.server.app.infrastructure.auth.AuthenticationPayload
import io.vitalir.kotlinhub.server.app.infrastructure.config.AppConfig
import java.util.*
import java.util.concurrent.TimeUnit

internal fun Route.loginRoute(
    jwtConfig: AppConfig.Jwt,
    loginUseCase: LoginUseCase,
) {
    post("auth/") {
        val loginRequest = call.receive<LoginRequest>()
        val loginResult = loginUseCase(
            UserCredentials(
                identifier = UserIdentifier.Login(loginRequest.login),
                password = loginRequest.password,
            )
        )
        val responseData = getResponseData(jwtConfig, loginResult)
        call.respondWith(responseData)
    }
}

private fun getResponseData(jwtConfig: AppConfig.Jwt, loginResult: Either<UserError, User>): ResponseData {
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

private fun createToken(jwtConfig: AppConfig.Jwt, userId: UserId): String {
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
