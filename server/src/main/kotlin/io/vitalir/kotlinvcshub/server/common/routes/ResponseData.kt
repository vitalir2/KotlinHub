package io.vitalir.kotlinvcshub.server.common.routes

import io.ktor.http.*

data class ResponseData(
    val code: HttpStatusCode,
    val body: Any,
) {

    companion object {
        fun fromErrorData(code: HttpStatusCode, errorMessage: String): ResponseData {
            return ResponseData(
                code = code,
                body = ErrorResponse(
                    code = code.value,
                    message = errorMessage,
                )
            )
        }
    }
}
