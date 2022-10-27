package io.vitalir.kotlinvcshub.server.user.routes.registration

import kotlinx.serialization.Serializable

@Serializable
internal class RegisterUserRequest(
    val login: String,
    val password: String,
)
