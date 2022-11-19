package io.vitalir.kotlinhub.server.app.feature.user.routes.update

import arrow.core.Either
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.extensions.respondWith
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.UpdateUserUseCase
import io.vitalir.kotlinhub.server.app.infrastructure.auth.userId

internal fun Route.updateCurrentUser(
    updateUserUseCase: UpdateUserUseCase,
) {
    put {
        val userId = call.userId
        val request = call.receive<UpdateCurrentUserRequest>()

        val result = updateUserUseCase(
            userId = userId,
            username = request.username,
            email = request.email,
        )

        val responseData = when (result) {
            is Either.Left -> result.value.toResponseData()
            is Either.Right -> ResponseData(
                code = HttpStatusCode.OK,
            )
        }
        call.respondWith(responseData)
    }
}

private fun UpdateUserUseCase.Error.toResponseData(): ResponseData {
    return when (this) {
        is UpdateUserUseCase.Error.InvalidArguments -> {
            ResponseData.fromErrorData(
                code = HttpStatusCode.BadRequest,
                errorMessage = message,
            )
        }

        is UpdateUserUseCase.Error.NoUser -> {
            ResponseData.fromErrorData(
                code = HttpStatusCode.BadRequest,
                errorMessage = "no user with id=$userId"
            )
        }
    }
}
