package io.vitalir.kotlinvcshub.server.user.data

import io.vitalir.kotlinvcshub.server.user.domain.password.PasswordManager
import org.mindrot.jbcrypt.BCrypt

internal class BCryptPasswordManager : PasswordManager {
    
    override fun encode(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    override fun comparePasswords(plaintext: String, hashed: String): Boolean {
        return BCrypt.checkpw(plaintext, hashed)
    }
}
