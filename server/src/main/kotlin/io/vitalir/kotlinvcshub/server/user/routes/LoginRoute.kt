package io.vitalir.kotlinvcshub.server.user.routes

import arrow.core.Either
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.vitalir.kotlinvcshub.server.common.routes.ResponseData
import io.vitalir.kotlinvcshub.server.common.routes.extensions.respondByResponseData
import io.vitalir.kotlinvcshub.server.infrastructure.config.AppConfig
import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvcshub.server.user.domain.model.UserCredentials
import io.vitalir.kotlinvcshub.server.user.domain.model.UserError
import io.vitalir.kotlinvcshub.server.user.domain.usecase.LoginUseCase
import java.util.*

internal fun Route.loginRoute(
    jwtConfig: AppConfig.Jwt,
    loginUseCase: LoginUseCase,
) {
    post("auth/") {
        val loginRequest = call.receive<LoginRequest>()
        val loginResult = loginUseCase(
            UserCredentials(
                identifier = UserCredentials.Identifier.Login(loginRequest.login),
                password = loginRequest.password,
            )
        )
        val responseData = getResponseData(jwtConfig, loginResult)
        call.respondByResponseData(responseData)
    }
}

private fun getResponseData(jwtConfig: AppConfig.Jwt, loginResult: Either<UserError, User>): ResponseData {
    return when (loginResult) {
        is Either.Left -> {
            getErrorResponseData(loginResult.value)
        }
        is Either.Right -> {
            val user = loginResult.value
            val token = JWT.create().apply {
                withAudience(jwtConfig.audience)
                withIssuer(jwtConfig.issuer)
                withClaim("login", user.login)
                withExpiresAt(Date(System.currentTimeMillis() + HOUR_MS))
            }.sign(Algorithm.HMAC256(jwtConfig.secret))
            ResponseData(
                code = HttpStatusCode.OK,
                body = LoginResponse(
                    userId = user.id,
                    token = token,
                ),
            )
        }
    }
}
