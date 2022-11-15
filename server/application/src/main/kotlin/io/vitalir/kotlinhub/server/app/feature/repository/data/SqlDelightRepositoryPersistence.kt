package io.vitalir.kotlinhub.server.app.feature.repository.data

import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryId
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.UpdateRepositoryData
import io.vitalir.kotlinhub.shared.feature.user.UserId
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.infrastructure.database.sqldelight.MainSqlDelight
import io.vitalir.kotlinvschub.server.infrastructure.database.sqldelight.RepositoriesQueries

internal class SqlDelightRepositoryPersistence(
    private val mainDatabase: MainSqlDelight,
) : RepositoryPersistence {

    private val queries: RepositoriesQueries
        get() = mainDatabase.repositoriesQueries

    override suspend fun isRepositoryExists(userId: UserId, name: String): Boolean {
        return queries.getRepositoryByUserIdAndName(userId, name)
            .executeAsOneOrNull() != null
    }

    override suspend fun isRepositoryExists(userIdentifier: UserIdentifier, name: String): Boolean {
        TODO()
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
        if (userIdentifier !is UserIdentifier.Id) return null // TODO remove and use all identifiers
        return queries.getRepositoryByUserIdAndNameJoined(
            userId = userIdentifier.value,
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
        TODO("Not yet implemented")
    }
}
