package io.vitalir.kotlinvcshub.server.user.routes

import io.vitalir.kotlinvcshub.server.user.domain.model.User

internal data class GetUserByLoginResponse(
    val user: User,
)
