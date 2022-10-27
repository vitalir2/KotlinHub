package io.vitalir.kotlinvcshub.server.user.routes.registration

import io.vitalir.kotlinvcshub.server.user.domain.model.UserId
import kotlinx.serialization.Serializable

@Serializable
internal data class RegisterUserResponse(
    val userId: UserId,
)
