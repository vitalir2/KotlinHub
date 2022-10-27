package io.vitalir.kotlinvcshub.server.user.routes.login

internal data class LoginRequest(
    val login: String,
    val password: String,
)
