package io.vitalir.kotlinhub.server.app.feature.user.routes.login

import kotlinx.serialization.Serializable

@Serializable
internal data class LoginRequest(
    val login: String,
    val password: String,
)
