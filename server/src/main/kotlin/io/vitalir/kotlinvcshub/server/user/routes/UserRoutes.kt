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
import io.vitalir.kotlinvcshub.server.user.domain.LoginError
import io.vitalir.kotlinvcshub.server.user.domain.LoginUseCase
import io.vitalir.kotlinvcshub.server.user.domain.RegisterUserUseCase
import io.vitalir.kotlinvcshub.server.user.domain.RegistrationError
import io.vitalir.kotlinvcshub.server.user.domain.User
import java.util.*

internal fun Routing.userRoutes(
    jwtConfig: AppConfig.Jwt,
    registerUserUseCase: RegisterUserUseCase,
    loginUseCase: LoginUseCase,
) {
    route("users/") {
        registerUserRoute(registerUserUseCase)
        loginRoute(jwtConfig, loginUseCase)
    }
}

private fun Route.registerUserRoute(registerUserUseCase: RegisterUserUseCase) {
    post {
        val registerUserRequest = call.receive<RegisterUserRequest>()
        val registrationResult = registerUserUseCase(
            User.Credentials(
                identifier = User.Credentials.Identifier.Login(registerUserRequest.login),
                password = registerUserRequest.password,
            )
        )
        val responseData: ResponseData = getResponseData(registrationResult)
        call.respond(responseData)
    }
}

@JvmName("getRegisterUserResponseData")
private fun getResponseData(registrationResult: Either<RegistrationError, User>): ResponseData {
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

private fun getErrorResponseData(registrationError: RegistrationError): ResponseData {
    return when (registrationError) {
        RegistrationError.InvalidCredentialsFormat -> {
            val responseBody = ErrorResponse(
                code = HttpStatusCode.BadRequest.value,
                message = "invalid credentials format",
            )
            ResponseData(code = HttpStatusCode.BadRequest, body = responseBody)
        }

        RegistrationError.UserAlreadyExists -> {
            val responseBody = ErrorResponse(
                code = HttpStatusCode.BadRequest.value,
                message = "user already exists",
            )
            ResponseData(code = HttpStatusCode.BadRequest, body = responseBody)
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
            User.Credentials(
                identifier = User.Credentials.Identifier.Login(loginRequest.login),
                password = loginRequest.password,
            )
        )
        val responseData = getResponseData(jwtConfig, loginResult)
        call.respond(responseData)
    }
}

@JvmName("getLoginResponseData")
private fun getResponseData(jwtConfig: AppConfig.Jwt, loginResult: Either<LoginError, User>): ResponseData {
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

private fun getErrorResponseData(loginError: LoginError): ResponseData {
    return when (loginError) {
        LoginError.InvalidCredentials -> {
            ResponseData(
                code = HttpStatusCode.BadRequest,
                body = ErrorResponse(
                    code = HttpStatusCode.BadRequest.value,
                    message = "invalid credentials",
                )
            )
        }
        LoginError.InvalidCredentialsFormat -> {
            ResponseData(
                code = HttpStatusCode.BadRequest,
                body = ErrorResponse(
                    code = HttpStatusCode.BadRequest.value,
                    message = "invalid credentials format",
                )
            )
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
