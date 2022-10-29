package io.vitalir.kotlinvcshub.server.repository.domain.persistence

import io.vitalir.kotlinvcshub.server.repository.domain.model.Repository
import io.vitalir.kotlinvcshub.server.user.domain.model.UserId

interface RepositoryPersistence {

    suspend fun isRepositoryExists(
        userId: UserId,
        name: String,
    ): Boolean

    suspend fun addRepository(repository: Repository)
}
