package io.vitalir.kotlinhub.server.app.infrastructure.auth

import io.vitalir.kotlinhub.server.app.user.domain.model.User

interface BasicAuthManager {

    fun checkCredentials(user: User, credentialsInBase64: String): Boolean
}
