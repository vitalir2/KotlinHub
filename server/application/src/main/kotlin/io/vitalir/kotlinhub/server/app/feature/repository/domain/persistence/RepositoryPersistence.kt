package io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence

import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserId

interface RepositoryPersistence {

    suspend fun isRepositoryExists(
        userId: UserId,
        name: String,
    ): Boolean

    suspend fun addRepository(repository: Repository): Int

    suspend fun getRepository(
        username: String,
        repositoryName: String,
    ): Repository?

    suspend fun removeRepository(repositoryId: Int)
}
