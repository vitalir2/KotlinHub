package io.vitalir.kotlinvcshub.server.user.routes

import arrow.core.Either
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.vitalir.kotlinvcshub.server.common.routes.ErrorResponse
import io.vitalir.kotlinvcshub.server.common.routes.ResponseData
import io.vitalir.kotlinvcshub.server.user.domain.RegisterUserUseCase
import io.vitalir.kotlinvcshub.server.user.domain.RegistrationError
import io.vitalir.kotlinvcshub.server.user.domain.User

fun Routing.userRoutes(registerUserUseCase: RegisterUserUseCase) {
    route("users/") {
        registerUserRoute(registerUserUseCase)
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
        call.respond(status = responseData.code, responseData.body)
    }
}

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
