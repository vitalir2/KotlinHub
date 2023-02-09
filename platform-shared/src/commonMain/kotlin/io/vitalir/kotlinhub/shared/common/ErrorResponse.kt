package io.vitalir.kotlinhub.shared.common

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@Serializable
@JsExport
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
