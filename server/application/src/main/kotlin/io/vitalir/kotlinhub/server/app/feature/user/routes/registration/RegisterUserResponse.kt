package io.vitalir.kotlinhub.server.app.feature.user.routes.registration

import io.vitalir.kotlinhub.shared.feature.user.UserId
import kotlinx.serialization.Serializable

@Serializable
internal data class RegisterUserResponse(
    val userId: UserId,
)
