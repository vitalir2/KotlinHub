package io.vitalir.kotlinvcshub.server.common.routes

import io.ktor.http.*

data class ResponseData(
    val code: HttpStatusCode,
    val body: Any,
)
