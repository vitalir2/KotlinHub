package io.vitalir.kotlinhub.server.app.infrastructure.auth

import io.vitalir.kotlinhub.server.app.user.domain.model.User

interface AuthManager {

    fun checkCredentials(user: User, credentials: String): Boolean
}
