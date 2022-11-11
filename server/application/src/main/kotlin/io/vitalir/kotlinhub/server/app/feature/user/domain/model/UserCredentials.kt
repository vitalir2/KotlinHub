package io.vitalir.kotlinhub.server.app.feature.user.domain.model

data class UserCredentials(
    val identifier: UserIdentifier,
    val password: String,
)
