package io.vitalir.kotlinvcshub.server.repository.domain

import io.vitalir.kotlinvcshub.server.user.domain.model.UserId

interface RepositoryPersistence {

    suspend fun isRepositoryExists(
        userId: UserId,
        name: String,
    ): Boolean
}
