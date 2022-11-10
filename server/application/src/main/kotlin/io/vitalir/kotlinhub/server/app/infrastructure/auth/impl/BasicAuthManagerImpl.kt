package io.vitalir.kotlinhub.server.app.infrastructure.auth.impl

import io.vitalir.kotlinhub.server.app.infrastructure.auth.BasicAuthManager
import io.vitalir.kotlinhub.server.app.infrastructure.encoding.Base64Manager
import io.vitalir.kotlinhub.server.app.user.domain.model.User
import io.vitalir.kotlinhub.server.app.user.domain.password.PasswordManager

internal class BasicAuthManagerImpl(
    private val base64Manager: Base64Manager,
    private val passwordManager: PasswordManager,
) : BasicAuthManager {

    override fun checkCredentials(user: User, credentialsInBase64: String): Boolean {
        val (_, credentialsPassword) = base64Manager.decode(credentialsInBase64)
            .split(":")
        return passwordManager.comparePasswords(credentialsPassword, user.password)
    }
}
