package io.vitalir.kotlinvcshub.server.user.routes

import arrow.core.Either
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.vitalir.kotlinvcshub.server.common.routes.ErrorResponse
import io.vitalir.kotlinvcshub.server.common.routes.ResponseData
import io.vitalir.kotlinvcshub.server.infrastructure.config.AppConfig
import io.vitalir.kotlinvcshub.server.infrastructure.di.AppGraph
import io.vitalir.kotlinvcshub.server.user.domain.usecase.LoginUseCase
import io.vitalir.kotlinvcshub.server.user.domain.usecase.RegisterUserUseCase
import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvcshub.server.user.domain.model.UserCredentials
import io.vitalir.kotlinvcshub.server.user.domain.model.UserError
import java.util.*

internal fun Routing.userRoutes(
    jwtConfig: AppConfig.Jwt,
    userGraph: AppGraph.User,
) {
    route("users/") {
        registerUserRoute(userGraph.registerUserUseCase)
        loginRoute(jwtConfig, userGraph.loginUseCase)
        getUserByLoginRoute(userGraph.getUserByLoginUseCase)
    }
}

private fun Route.registerUserRoute(registerUserUseCase: RegisterUserUseCase) {
    post {
        val registerUserRequest = call.receive<RegisterUserRequest>()
        val registrationResult = registerUserUseCase(
            UserCredentials(
                identifier = UserCredentials.Identifier.Login(registerUserRequest.login),
                password = registerUserRequest.password,
            )
        )
        val responseData: ResponseData = getResponseData(registrationResult)
        call.respond(responseData)
    }
}

@JvmName("getRegisterUserResponseData")
private fun getResponseData(registrationResult: Either<UserError, User>): ResponseData {
    return when (registrationResult) {
        is Either.Left -> {
            getErrorResponseData(registrationResult.value)
        }
        is Either.Right -> {
            val responseBody = RegisterUserResponse(userId = registrationResult.value.id)
            ResponseData(code = HttpStatusCode.Created, body = responseBody)
        }
    }
}

private fun Route.loginRoute(
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
        call.respond(responseData)
    }
}

@JvmName("getLoginResponseData")
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

private fun getErrorResponseData(userError: UserError): ResponseData {
    return when (userError) {
        is UserError.InvalidCredentials -> {
            ResponseData(
                code = HttpStatusCode.BadRequest,
                body = ErrorResponse(
                    code = HttpStatusCode.BadRequest.value,
                    message = "invalid credentials",
                )
            )
        }
        is UserError.ValidationFailed -> {
            ResponseData(
                code = HttpStatusCode.BadRequest,
                body = ErrorResponse(
                    code = HttpStatusCode.BadRequest.value,
                    message = "invalid credentials format",
                )
            )
        }
        is UserError.UserAlreadyExists -> {
            val responseBody = ErrorResponse(
                code = HttpStatusCode.BadRequest.value,
                message = "user already exists",
            )
            ResponseData(code = HttpStatusCode.BadRequest, body = responseBody)
        }
    }
}

private suspend fun ApplicationCall.respond(responseData: ResponseData) {
    respond(
        status = responseData.code,
        message = responseData.body,
    )
}

private const val HOUR_MS = 1000 * 60 * 60
