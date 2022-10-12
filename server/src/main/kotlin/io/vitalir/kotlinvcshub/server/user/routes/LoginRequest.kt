package io.vitalir.kotlinvcshub.server.user.routes

internal data class LoginRequest(
    val login: String,
    val password: String,
)
