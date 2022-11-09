package io.vitalir.kotlinhub.server.app.repository.data

import io.vitalir.kotlinhub.server.app.infrastructure.database.sqldelight.MainSqlDelight
import io.vitalir.kotlinhub.server.app.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinhub.server.app.user.domain.model.UserId
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

    override suspend fun addRepository(repository: Repository) {
        queries.insertRepository(
            user_id = repository.owner.id,
            name = repository.name,
            access_mode = repository.accessMode.toDataModel(),
            created_at = repository.createdAt,
            updated_at = repository.updatedAt,
            description = repository.description,
        )
    }

    override suspend fun getRepository(username: String, repositoryName: String): Repository? {
        return queries.getRepositoryByUsernameAndRepositoryName(
            username = username,
            repositoryName = repositoryName,
        ).executeAsOneOrNull()?.toDomainModel()
    }

    companion object {
        private fun Repository.AccessMode.toDataModel(): Int = when (this) {
            Repository.AccessMode.PUBLIC -> 0
            Repository.AccessMode.PRIVATE -> 1
        }
    }
}
