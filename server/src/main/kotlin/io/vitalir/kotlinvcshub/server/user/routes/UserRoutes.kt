package io.vitalir.kotlinvcshub.server.user.routes

import arrow.core.Either
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.vitalir.kotlinvcshub.server.common.routes.ErrorResponse
import io.vitalir.kotlinvcshub.server.common.routes.ResponseData
import io.vitalir.kotlinvcshub.server.user.domain.LoginError
import io.vitalir.kotlinvcshub.server.user.domain.LoginUseCase
import io.vitalir.kotlinvcshub.server.user.domain.RegisterUserUseCase
import io.vitalir.kotlinvcshub.server.user.domain.RegistrationError
import io.vitalir.kotlinvcshub.server.user.domain.User

internal fun Routing.userRoutes(
    registerUserUseCase: RegisterUserUseCase,
    loginUseCase: LoginUseCase,
) {
    route("users/") {
        registerUserRoute(registerUserUseCase)
        loginRoute(loginUseCase)
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

private fun Route.loginRoute(loginUseCase: LoginUseCase) {
    post("auth/") {
        val loginRequest = call.receive<LoginRequest>()
        val loginResult = loginUseCase(
            User.Credentials(
                identifier = User.Credentials.Identifier.Login(loginRequest.login),
                password = loginRequest.password,
            )
        )
        val responseData = getResponseData(loginResult)
        call.respond(responseData)
    }
}

@JvmName("getLoginResponseData")
private fun getResponseData(loginResult: Either<LoginError, User>): ResponseData {
    return when (loginResult) {
        is Either.Left -> {
            getErrorResponseData(loginResult.value)
        }
        is Either.Right -> {
            ResponseData(
                code = HttpStatusCode.OK,
                body = LoginResponse(userId = loginResult.value.id),
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
