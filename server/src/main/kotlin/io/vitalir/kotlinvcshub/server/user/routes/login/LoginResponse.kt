package io.vitalir.kotlinvcshub.server.user.routes.login

import kotlinx.serialization.Serializable

@Serializable
internal data class LoginResponse(
    val token: String,
)
