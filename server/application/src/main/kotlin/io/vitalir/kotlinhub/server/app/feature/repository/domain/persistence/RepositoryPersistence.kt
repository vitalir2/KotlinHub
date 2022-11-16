package io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence

import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.UpdateRepositoryData
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.shared.feature.user.UserId

interface RepositoryPersistence {

    suspend fun isRepositoryExists(
        userIdentifier: UserIdentifier,
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

    fun updateRepository(
        userIdentifier: UserIdentifier,
        repositoryName: String,
        updateRepositoryData: UpdateRepositoryData,
    )
}
