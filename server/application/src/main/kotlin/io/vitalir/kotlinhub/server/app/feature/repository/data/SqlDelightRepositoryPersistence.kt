package io.vitalir.kotlinhub.server.app.feature.repository.data

import io.vitalir.kotlinhub.server.app.common.domain.LocalDateTimeProvider
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryId
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryIdentifier
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.UpdateRepositoryData
import io.vitalir.kotlinhub.server.app.feature.user.data.UserIdentifierConverter
import io.vitalir.kotlinhub.server.app.feature.user.data.extensions.toDomainModel
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.infrastructure.database.sqldelight.MainSqlDelight
import io.vitalir.kotlinhub.shared.feature.user.UserId
import io.vitalir.kotlinvschub.server.infrastructure.database.sqldelight.RepositoriesQueries
import java.time.LocalDateTime

internal class SqlDelightRepositoryPersistence(
    private val mainDatabase: MainSqlDelight,
    private val userIdentifierConverter: UserIdentifierConverter,
    private val localDateTimeProvider: LocalDateTimeProvider,
) : RepositoryPersistence {

    private val queries: RepositoriesQueries
        get() = mainDatabase.repositoriesQueries

    override suspend fun isRepositoryExists(repositoryIdentifier: RepositoryIdentifier): Boolean =
        when (repositoryIdentifier) {
            is RepositoryIdentifier.Id -> queries.getRepositoryByIdJoined(repositoryIdentifier.value)
                .executeAsOneOrNull() != null
            is RepositoryIdentifier.OwnerIdentifierAndName -> queries.getRepositoryByUserIdAndName(
                user_id = userIdentifierConverter.convertToUserId(repositoryIdentifier.ownerIdentifier),
                name = repositoryIdentifier.repositoryName,
            ).executeAsOneOrNull() != null
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

    override suspend fun getRepository(
        repositoryIdentifier: RepositoryIdentifier,
    ): Repository? {
        return when (repositoryIdentifier) {
            is RepositoryIdentifier.Id -> getRepository(repositoryIdentifier.value)
            is RepositoryIdentifier.OwnerIdentifierAndName -> getRepository(
                repositoryIdentifier.ownerIdentifier,
                repositoryIdentifier.repositoryName,
            )
        }
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
        mainDatabase.transaction {
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
                    updatedAt = updatedAt.toLocalDateTime(localDateTimeProvider),
                    userId = ownerId,
                    name = repositoryName,
                )
            }
        }
    }

    override suspend fun getRepositories(
        userIdentifier: UserIdentifier,
        accessModes: List<Repository.AccessMode>,
    ): List<Repository> {
        val userId = userIdentifierConverter.convertToUserId(userIdentifier)
        val user = mainDatabase.cUsersQueries.getById(userId).executeAsOne().toDomainModel()
        return queries.getRepositoriesByUserId(
            userId = userId,
            accessModes = accessModes.map(Repository.AccessMode::asInt),
        )
            .executeAsList()
            .map { it.toDomainModel(user) }
    }

    private fun getRepository(repositoryId: RepositoryId): Repository? {
        return queries.getRepositoryByIdJoined(repositoryId)
            .executeAsOneOrNull()
            ?.toDomainModel()
    }

    companion object {
        private fun UpdateRepositoryData.Time.toLocalDateTime(
            localDateTimeProvider: LocalDateTimeProvider,
        ): LocalDateTime {
            return when (this) {
                is UpdateRepositoryData.Time.Custom -> time
                is UpdateRepositoryData.Time.Now -> localDateTimeProvider.now()
            }
        }
    }
}
