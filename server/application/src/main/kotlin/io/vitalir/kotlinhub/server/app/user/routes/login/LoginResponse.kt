package io.vitalir.kotlinhub.server.app.user.routes.login

import kotlinx.serialization.Serializable

@Serializable
internal data class LoginResponse(
    val token: String,
)
