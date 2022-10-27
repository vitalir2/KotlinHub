package io.vitalir.kotlinvcshub.server.user.routes.login

import io.vitalir.kotlinvcshub.server.user.domain.model.UserId
import kotlinx.serialization.Serializable

@Serializable
internal data class LoginResponse(
    val userId: UserId,
    val token: String,
)
