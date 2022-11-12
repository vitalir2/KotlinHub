package io.vitalir.kotlinhub.server.app.feature.user.routes.login

import kotlinx.serialization.Serializable

@Serializable
internal data class LoginRequest(
    val username: String,
    val password: String,
)
