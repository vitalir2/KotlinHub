package io.vitalir.kotlinvcshub.server.user.domain.password

/**
 * Interface handling password management
 * @author vitalir
 */
interface PasswordManager {

    /**
     * Encode password
     * @param password password to encode
     * @return encoded password in a specified by its implementation encoding
     */
    fun encode(password: String): String

    /**
     * @param password outer password e.g. from user input
     * @param hashedPassword as its name implies, a hashed password e.g. from database
     * @return true if two are the same password in their raw form
     */
    fun comparePasswords(password: String, hashedPassword: String): Boolean
}
