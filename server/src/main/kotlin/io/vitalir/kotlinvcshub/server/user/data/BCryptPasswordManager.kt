package io.vitalir.kotlinvcshub.server.user.data

import io.vitalir.kotlinvcshub.server.user.domain.password.PasswordManager

// TODO
internal class BCryptPasswordManager : PasswordManager {
    
    override fun encode(password: String): String {
        return ""
    }

    override fun comparePasswords(password: String, hashedPassword: String): Boolean {
        return false
    }
}
