package io.vitalir.kotlinhub.server.app.common.routes

import io.ktor.http.*
import io.vitalir.kotlinhub.shared.common.ErrorResponse

data class ResponseData(
    val code: HttpStatusCode,
    val body: Any? = null,
) {

    companion object {

        fun ok(): ResponseData {
            return ResponseData(
                code = HttpStatusCode.OK,
            )
        }
        fun fromErrorData(code: HttpStatusCode, errorMessage: String): ResponseData {
            return ResponseData(
                code = code,
                body = ErrorResponse(
                    code = code.value,
                    message = errorMessage,
                )
            )
        }

        fun badRequest(message: String? = null): ResponseData {
            return fromErrorData(
                code = HttpStatusCode.BadRequest,
                errorMessage = message ?: "bad request",
            )
        }

        fun serverError(): ResponseData {
            return fromErrorData(
                code = HttpStatusCode.InternalServerError,
                errorMessage = "internal server error",
            )
        }

        fun forbidden(message: String? = null): ResponseData {
            return fromErrorData(
                code = HttpStatusCode.Forbidden,
                errorMessage = message ?: "forbidded",
            )
        }
    }
}
