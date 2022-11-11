package io.vitalir.kotlinhub.server.app.infrastructure.auth

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
     * @param plaintext outer password e.g. from user input
     * @param hashed as its name implies, a hashed password e.g. from database
     * @return true if two are the same password in their raw form, false otherwise
     */
    fun comparePasswords(plaintext: String, hashed: String): Boolean
}
