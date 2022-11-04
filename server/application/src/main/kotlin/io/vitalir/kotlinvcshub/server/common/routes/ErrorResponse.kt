package io.vitalir.kotlinvcshub.server.common.routes

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val code: Int,
    val message: String,
) {

    companion object {
        fun unauthorized(): ErrorResponse {
            return ErrorResponse(
                code = 401,
                message = "unauthorized",
            )
        }
    }
}
