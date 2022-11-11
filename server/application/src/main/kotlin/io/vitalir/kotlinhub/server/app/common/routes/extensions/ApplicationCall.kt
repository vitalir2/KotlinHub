package io.vitalir.kotlinhub.server.app.common.routes.extensions

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData

internal suspend fun ApplicationCall.respondWith(responseData: ResponseData) {
    val message = responseData.body
    if (message != null) {
        respond(
            status = responseData.code,
            message = responseData.body,
        )
    } else {
        respond(
            message = responseData.code,
        )
    }
}
