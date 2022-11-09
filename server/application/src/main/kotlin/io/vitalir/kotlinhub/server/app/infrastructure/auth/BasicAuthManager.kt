package io.vitalir.kotlinhub.server.app.infrastructure.auth

import io.vitalir.kotlinhub.server.app.user.domain.model.User

interface BasicAuthManager {

    // TODO remove header value parsing logic to kgit
    fun checkCredentials(user: User, headerValue: String?): Boolean
}
