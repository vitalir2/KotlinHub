package io.vitalir.kotlinhub.server.app.repository.domain.persistence

import io.vitalir.kotlinhub.server.app.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.user.domain.model.UserId

interface RepositoryPersistence {

    suspend fun isRepositoryExists(
        userId: UserId,
        name: String,
    ): Boolean

    suspend fun addRepository(repository: Repository)
}
