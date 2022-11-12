package io.vitalir.kotlinhub.server.app.common.routes

import io.ktor.http.*

data class ResponseData(
    val code: HttpStatusCode,
    val body: Any? = null,
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

        fun unauthorized(): ResponseData {
            return ResponseData(
                code = HttpStatusCode.Unauthorized,
                body = ErrorResponse.unauthorized(),
            )
        }

        fun emptyBody(): ResponseData {
            return fromErrorData(
                code = HttpStatusCode.BadRequest,
                errorMessage = "empty body",
            )
        }
    }
}
