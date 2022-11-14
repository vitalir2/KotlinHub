package io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence

import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserId
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier

interface RepositoryPersistence {

    suspend fun isRepositoryExists(
        userId: UserId,
        name: String,
    ): Boolean

    suspend fun addRepository(repository: Repository): Int

    suspend fun getRepository(
        userIdentifier: UserIdentifier,
        repositoryName: String,
    ): Repository?

    suspend fun removeRepositoryById(repositoryId: Int)

    suspend fun removeRepositoryByName(
        userId: UserId,
        repositoryName: String,
    )
}
