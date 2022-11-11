package io.vitalir.kotlinhub.server.app.infrastructure.auth.impl

import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinhub.server.app.infrastructure.auth.PasswordManager
import io.vitalir.kotlinhub.server.app.infrastructure.auth.AuthManager
import io.vitalir.kotlinhub.server.app.infrastructure.encoding.Base64Manager

internal class Base64AuthManager(
    private val base64Manager: Base64Manager,
    private val passwordManager: PasswordManager,
) : AuthManager {

    override fun checkCredentials(user: User, credentials: String): Boolean {
        val (_, credentialsPassword) = base64Manager.decode(credentials)
            .split(":")
        return passwordManager.comparePasswords(credentialsPassword, user.password)
    }
}
