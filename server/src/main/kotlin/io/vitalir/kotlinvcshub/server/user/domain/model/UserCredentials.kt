package io.vitalir.kotlinvcshub.server.user.domain.model

data class UserCredentials(
    val identifier: Identifier,
    val password: String,
) {

    sealed interface Identifier {

        @JvmInline
        value class Login(val value: String) : Identifier

        @JvmInline
        value class Email(val value: String) : Identifier
    }
}
