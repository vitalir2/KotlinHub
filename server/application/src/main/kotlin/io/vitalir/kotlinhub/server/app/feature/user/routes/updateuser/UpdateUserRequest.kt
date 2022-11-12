package io.vitalir.kotlinhub.server.app.feature.user.routes.updateuser

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRequest(
    val username: String? = null,
    val email: String? = null,
)
