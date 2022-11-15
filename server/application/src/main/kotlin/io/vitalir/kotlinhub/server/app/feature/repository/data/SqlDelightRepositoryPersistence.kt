package io.vitalir.kotlinhub.server.app.feature.repository.data

import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryId
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryIdentifier
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.UpdateRepositoryData
import io.vitalir.kotlinhub.shared.feature.user.UserId
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.infrastructure.database.sqldelight.MainSqlDelight
import io.vitalir.kotlinvschub.server.infrastructure.database.sqldelight.CUsersQueries
import io.vitalir.kotlinvschub.server.infrastructure.database.sqldelight.RepositoriesQueries
import java.time.LocalDateTime

// TODO create method for converting email / login -> uid
internal class SqlDelightRepositoryPersistence(
    private val mainDatabase: MainSqlDelight,
) : RepositoryPersistence {

    private val queries: RepositoriesQueries
        get() = mainDatabase.repositoriesQueries

    private val userQueries: CUsersQueries
        get() = mainDatabase.cUsersQueries

    override suspend fun isRepositoryExists(userId: UserId, name: String): Boolean {
        return queries.getRepositoryByUserIdAndName(userId, name)
            .executeAsOneOrNull() != null
    }

    override suspend fun isRepositoryExists(userIdentifier: UserIdentifier, name: String): Boolean {
        return when (userIdentifier) {
            is UserIdentifier.Id -> isRepositoryExists(userIdentifier.value, name)
            is UserIdentifier.Username -> isRepositoryExists(userIdentifier, name)
            is UserIdentifier.Email -> isRepositoryExists(userIdentifier, name)
        }
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

    private fun isRepositoryExists(
        username: UserIdentifier.Username,
        repositoryName: String,
    ): Boolean {
        return mainDatabase.transactionWithResult {
            val userId = userQueries.getUserIdByUsername(username.value).executeAsOne()
            queries.getRepositoryByUserIdAndName(userId, repositoryName).executeAsOneOrNull() != null
        }
    }

    private fun isRepositoryExists(
        email: UserIdentifier.Email,
        repositoryName: String,
    ): Boolean {
        return mainDatabase.transactionWithResult {
            val userId = userQueries.getUserIdByEmail(email.value).executeAsOne()
            queries.getRepositoryByUserIdAndName(userId, repositoryName).executeAsOneOrNull() != null
        }
    }

    override fun updateRepository(
        userIdentifier: UserIdentifier,
        repositoryName: String,
        updateRepositoryData: UpdateRepositoryData,
    ) {
        updateRepositoryData.accessMode?.let {
            updateAccessMode(
                repositoryIdentifier = RepositoryIdentifier.OwnerIdentifierAndName(userIdentifier, repositoryName),
                accessMode = it,
            )
        }
        updateRepositoryData.updatedAt?.let {
            updateUpdatedAt(
                repositoryIdentifier = RepositoryIdentifier.OwnerIdentifierAndName(userIdentifier, repositoryName),
                updatedAt = it,
            )
        }
    }

    private fun updateAccessMode(
        repositoryIdentifier: RepositoryIdentifier.OwnerIdentifierAndName,
        accessMode: Repository.AccessMode,
    ) {
        mainDatabase.transaction {
            when (repositoryIdentifier.ownerIdentifier) {
                is UserIdentifier.Id -> queries.updateRepositoryAccessMode(
                    accessMode = accessMode.asInt(),
                    userId = repositoryIdentifier.ownerIdentifier.value,
                    name = repositoryIdentifier.repositoryName,
                )
                is UserIdentifier.Email,
                is UserIdentifier.Username,
                    -> TODO()
            }
        }
    }

    private fun updateUpdatedAt(
        repositoryIdentifier: RepositoryIdentifier.OwnerIdentifierAndName,
        updatedAt: LocalDateTime,
    ) {
        mainDatabase.transaction {
            when (repositoryIdentifier.ownerIdentifier) {
                is UserIdentifier.Id -> queries.updateRepositoryUpdatedAt(
                    updatedAt = updatedAt,
                    userId = repositoryIdentifier.ownerIdentifier.value,
                    name = repositoryIdentifier.repositoryName,
                )
                is UserIdentifier.Email,
                is UserIdentifier.Username,
                    -> TODO()
            }
        }
    }
}
