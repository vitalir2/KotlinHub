package io.vitalir.kotlinvcshub.server.user.domain

import io.vitalir.kotlinvcshub.server.repository.domain.Repository

data class User(
    val login: String,
    val password: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val repositories: List<Repository> = emptyList(),
)
