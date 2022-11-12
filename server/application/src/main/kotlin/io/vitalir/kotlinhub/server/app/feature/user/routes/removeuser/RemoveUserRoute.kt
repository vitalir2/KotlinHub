package io.vitalir.kotlinhub.server.app.feature.user.routes.removeuser

import arrow.core.Either
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.extensions.respondWith
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.RemoveUserUseCase
import io.vitalir.kotlinhub.server.app.infrastructure.auth.userId

internal fun Route.removeUser(
    removeUserUseCase: RemoveUserUseCase,
) {
    delete {
        val userId = call.userId ?: run {
            call.respondWith(ResponseData.unauthorized())
            return@delete
        }

        when(val result = removeUserUseCase(userId)) {
            is Either.Left -> {
                val errorResponseData = result.value.toErrorResponseData()
                call.respondWith(errorResponseData)
            }
            is Either.Right -> {
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}

private fun RemoveUserUseCase.Error.toErrorResponseData(): ResponseData {
    return when (this) {
        is RemoveUserUseCase.Error.RemoveFailed -> {
            ResponseData.fromErrorData(
                code = HttpStatusCode.InternalServerError,
                errorMessage = "remove failed",
            )
        }
        is RemoveUserUseCase.Error.UserDoesNotExist -> {
            ResponseData.fromErrorData(
                code = HttpStatusCode.BadRequest,
                errorMessage = "user with userId=$userId does not exist",
            )
        }
    }
}
