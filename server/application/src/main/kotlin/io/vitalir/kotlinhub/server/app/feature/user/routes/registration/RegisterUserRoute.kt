package io.vitalir.kotlinhub.server.app.feature.user.routes.registration

import arrow.core.Either
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.extensions.respondWith
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserCredentials
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserError
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.RegisterUserUseCase
import io.vitalir.kotlinhub.server.app.feature.user.routes.getErrorResponseData

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
        call.respondWith(responseData)
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
