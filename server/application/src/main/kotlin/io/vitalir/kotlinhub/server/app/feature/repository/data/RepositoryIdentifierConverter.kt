package io.vitalir.kotlinhub.server.app.feature.repository.data

import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.data.UserIdentifierConverter
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.infrastructure.database.sqldelight.MainSqlDelight

internal class RepositoryIdentifierConverter(
    private val sqlDelight: MainSqlDelight,
    private val userIdentifierConverter: UserIdentifierConverter,
) {

    fun getUserAndRepositoryName(
        repositoryIdentifier: RepositoryIdentifier,
    ): Pair<UserIdentifier.Id, String> {
        return when (repositoryIdentifier) {
            is RepositoryIdentifier.Id -> {
                val result = sqlDelight.repositoriesQueries
                    .getRepositoryByIdJoined(repositoryIdentifier.value)
                    .executeAsOneOrNull()

                if (result == null) {
                    throw Exception("Repository with identifier=$repositoryIdentifier does not exist")
                } else {
                    UserIdentifier.Id(result.user_id!!) to result.name
                }
            }

            is RepositoryIdentifier.OwnerIdentifierAndName -> {
                val userId = userIdentifierConverter.convertToUserId(repositoryIdentifier.ownerIdentifier)
                UserIdentifier.Id(userId) to repositoryIdentifier.repositoryName
            }
        }
    }
}
