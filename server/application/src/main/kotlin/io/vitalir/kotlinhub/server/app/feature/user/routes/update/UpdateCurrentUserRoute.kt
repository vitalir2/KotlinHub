package io.vitalir.kotlinhub.server.app.feature.user.routes.update

import arrow.core.Either
import io.bkbn.kompendium.core.metadata.PutInfo
import io.bkbn.kompendium.core.plugin.NotarizedRoute
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.extensions.respondWith
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.UpdateUserUseCase
import io.vitalir.kotlinhub.server.app.infrastructure.auth.userId
import io.vitalir.kotlinhub.server.app.infrastructure.docs.badRequestResponse
import io.vitalir.kotlinhub.server.app.infrastructure.docs.reqType
import io.vitalir.kotlinhub.server.app.infrastructure.docs.resType

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

internal fun NotarizedRoute.Config.updateCurrentUserDocs() {
    put = PutInfo.builder {
        summary("Update current user")
        description("")
        request {
            reqType<UpdateCurrentUserRequest>()
            description("Data to update")
        }
        response {
            resType<Int>()
            responseCode(HttpStatusCode.OK)
            description("OK status code")
        }
        badRequestResponse()
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
        is UpdateUserUseCase.Error.InvalidArgument -> ResponseData.badRequest(
            "argument ${nameToValue.first}=${nameToValue.second} is not valid"
        )

        is UpdateUserUseCase.Error.NoUser -> ResponseData.badRequest(
            "no user with id=$userId"
        )
        is UpdateUserUseCase.Error.Conflict -> ResponseData.badRequest(
            "conflict: user with ${nameToValue.first}=${nameToValue.second} already exists"
        )
        is UpdateUserUseCase.Error.NoUpdates -> ResponseData.badRequest(
            "no new values were passed"
        )
    }
}
