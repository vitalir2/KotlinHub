package io.vitalir.kotlinvcshub.server.user.domain

import io.vitalir.kotlinvcshub.server.repository.domain.Repository

data class User(
    val id: UserId,
    val login: String,
    val password: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val repositories: List<Repository> = emptyList(),
) {

    data class Credentials(
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
}
