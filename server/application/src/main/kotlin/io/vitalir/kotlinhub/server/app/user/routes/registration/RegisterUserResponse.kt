package io.vitalir.kotlinhub.server.app.user.routes.registration

import io.vitalir.kotlinhub.server.app.user.domain.model.UserId
import kotlinx.serialization.Serializable

@Serializable
internal data class RegisterUserResponse(
    val userId: UserId,
)
