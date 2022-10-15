package io.vitalir.kotlinvcshub.server.user.routes

import io.vitalir.kotlinvcshub.server.user.domain.UserId

internal data class LoginResponse(
    val userId: UserId,
    val token: String,
)
