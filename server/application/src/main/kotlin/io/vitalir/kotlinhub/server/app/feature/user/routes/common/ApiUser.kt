package io.vitalir.kotlinhub.server.app.feature.user.routes.common

import io.vitalir.kotlinhub.shared.feature.user.UserId
import kotlinx.serialization.Serializable

@Serializable
internal data class ApiUser(
    val id: UserId,
    val username: String,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
)
