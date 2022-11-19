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
            updateData = request.toUpdateData(),
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

private fun UpdateCurrentUserRequest.toUpdateData(): UpdateUserUseCase.UpdateData {
    return UpdateUserUseCase.UpdateData(
        username =  username.newValueOrOld(),
        email = email.newValueOrOld(),
    )
}

private fun <T> T?.newValueOrOld(): UpdateUserUseCase.UpdateData.Value<T> {
    return if (this !== null) {
        UpdateUserUseCase.UpdateData.Value.New(this)
    } else {
        UpdateUserUseCase.UpdateData.Value.Old
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
