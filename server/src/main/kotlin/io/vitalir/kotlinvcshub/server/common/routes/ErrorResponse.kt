package io.vitalir.kotlinvcshub.server.common.routes

data class ErrorResponse(
    val code: Int,
    val message: String,
)
