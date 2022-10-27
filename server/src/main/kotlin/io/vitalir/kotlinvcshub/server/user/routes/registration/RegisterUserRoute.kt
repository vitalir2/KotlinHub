package io.vitalir.kotlinvcshub.server.user.routes.registration

import arrow.core.Either
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.vitalir.kotlinvcshub.server.common.routes.ResponseData
import io.vitalir.kotlinvcshub.server.common.routes.extensions.respondByResponseData
import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvcshub.server.user.domain.model.UserCredentials
import io.vitalir.kotlinvcshub.server.user.domain.model.UserError
import io.vitalir.kotlinvcshub.server.user.domain.usecase.RegisterUserUseCase
import io.vitalir.kotlinvcshub.server.user.routes.getErrorResponseData

internal fun Route.registerUserRoute(registerUserUseCase: RegisterUserUseCase) {
    post {
        val registerUserRequest = call.receive<RegisterUserRequest>()
        val registrationResult = registerUserUseCase(
            UserCredentials(
                identifier = UserCredentials.Identifier.Login(registerUserRequest.login),
                password = registerUserRequest.password,
            )
        )
        val responseData: ResponseData = getResponseData(registrationResult)
        call.respondByResponseData(responseData)
    }
}

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
