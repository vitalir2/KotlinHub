package io.vitalir.kotlinhub.server.app.feature.repository.data

import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryId
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.UpdateRepositoryData
import io.vitalir.kotlinhub.server.app.feature.user.data.UserIdentifierConverter
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.infrastructure.database.sqldelight.MainSqlDelight
import io.vitalir.kotlinhub.shared.feature.user.UserId
import io.vitalir.kotlinvschub.server.infrastructure.database.sqldelight.RepositoriesQueries

internal class SqlDelightRepositoryPersistence(
    private val mainDatabase: MainSqlDelight,
    private val userIdentifierConverter: UserIdentifierConverter,
) : RepositoryPersistence {

    private val queries: RepositoriesQueries
        get() = mainDatabase.repositoriesQueries

    override suspend fun isRepositoryExists(userId: UserId, name: String): Boolean {
        return queries.getRepositoryByUserIdAndName(userId, name)
            .executeAsOneOrNull() != null
    }

    override suspend fun isRepositoryExists(userIdentifier: UserIdentifier, name: String): Boolean {
        val userId = userIdentifierConverter.convertToUserId(userIdentifier)
        return isRepositoryExists(userId, name)
    }

    override suspend fun addRepository(repository: Repository): RepositoryId {
        return queries.insertRepository(
            user_id = repository.owner.id,
            name = repository.name,
            access_mode = repository.accessMode.asInt(),
            created_at = repository.createdAt,
            updated_at = repository.updatedAt,
            description = repository.description,
        ).executeAsOne()
    }

    override suspend fun getRepository(
        userIdentifier: UserIdentifier,
        repositoryName: String
    ): Repository? {
        val userId = userIdentifierConverter.convertToUserId(userIdentifier)
        return queries.getRepositoryByUserIdAndNameJoined(
            userId = userId,
            repositoryName = repositoryName,
        ).executeAsOneOrNull()?.toDomainModel()
    }

    override suspend fun removeRepositoryById(repositoryId: Int) {
        queries.removeRepositoryById(repositoryId)
    }

    override suspend fun removeRepositoryByName(userId: UserId, repositoryName: String) {
        queries.removeRepositoryByName(userId, repositoryName)
    }

    override fun updateRepository(
        userIdentifier: UserIdentifier,
        repositoryName: String,
        updateRepositoryData: UpdateRepositoryData,
    ) {
        val ownerId = userIdentifierConverter.convertToUserId(userIdentifier)
        updateRepositoryData.accessMode?.let { accessMode ->
            queries.updateRepositoryAccessMode(
                accessMode = accessMode.asInt(),
                userId = ownerId,
                name = repositoryName,
            )
        }
        updateRepositoryData.updatedAt?.let { updatedAt ->
            queries.updateRepositoryUpdatedAt(
                updatedAt = updatedAt,
                userId = ownerId,
                name = repositoryName,
            )
        }
    }
}
