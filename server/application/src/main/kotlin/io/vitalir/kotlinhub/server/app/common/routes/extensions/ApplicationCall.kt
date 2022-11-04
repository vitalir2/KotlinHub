package io.vitalir.kotlinhub.server.app.common.routes.extensions

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData

internal suspend fun ApplicationCall.respondWith(responseData: ResponseData) {
    respond(
        status = responseData.code,
        message = responseData.body,
    )
}
