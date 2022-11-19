package io.vitalir.kotlinhub.server.app.feature.user.routes.update

import kotlinx.serialization.Serializable

@Serializable
data class UpdateCurrentUserRequest(
    val username: String? = null,
    val email: String? = null,
)
