package io.vitalir.kotlinvcshub.server.user.domain.model

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
    /**
     * For tests e.g. [io.vitalir.kotlinvschub.server.user.domain.extensionsKt]
     */
    companion object
}
