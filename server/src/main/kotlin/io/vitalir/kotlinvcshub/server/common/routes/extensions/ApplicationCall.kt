package io.vitalir.kotlinvcshub.server.common.routes.extensions

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.vitalir.kotlinvcshub.server.common.routes.ResponseData

internal suspend fun ApplicationCall.respondByResponseData(responseData: ResponseData) {
    respond(
        status = responseData.code,
        message = responseData.body,
    )
}
