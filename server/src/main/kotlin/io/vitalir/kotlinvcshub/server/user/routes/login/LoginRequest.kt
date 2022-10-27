package io.vitalir.kotlinvcshub.server.user.routes.login

import kotlinx.serialization.Serializable

@Serializable
internal data class LoginRequest(
    val login: String,
    val password: String,
)
