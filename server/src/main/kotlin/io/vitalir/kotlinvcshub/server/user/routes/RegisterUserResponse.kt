package io.vitalir.kotlinvcshub.server.user.routes

import io.vitalir.kotlinvcshub.server.user.domain.model.UserId

internal data class RegisterUserResponse(
    val userId: UserId,
)
