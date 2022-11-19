package io.vitalir.kotlinhub.server.app.feature.user.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.vitalir.kotlinhub.server.app.feature.user.data.extensions.toDomainModel
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserError
import io.vitalir.kotlinhub.shared.feature.user.UserId
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence
import io.vitalir.kotlinhub.server.app.infrastructure.database.sqldelight.MainSqlDelight
import io.vitalir.kotlinvschub.server.infrastructure.database.sqldelight.CUsersQueries
import io.vitalir.kotlinvschub.server.infrastructure.database.sqldelight.Users

internal class SqlDelightUserPersistence(
    private val sqlDelightDatabase: MainSqlDelight,
) : UserPersistence {

    private val queries: CUsersQueries
        get() = sqlDelightDatabase.cUsersQueries

    override suspend fun getUser(identifier: UserIdentifier): User? {
        // Faster without userIdentifierConverter, so let it be
        return when (identifier) {
            is UserIdentifier.Email -> queries.getByEmail(identifier.value)
            is UserIdentifier.Id -> queries.getById(identifier.value)
            is UserIdentifier.Username -> queries.getByUsername(identifier.value)
        }
            .executeAsOneOrNull()
            ?.toDomainModel()
    }

    override suspend fun addUser(user: User): Either<UserError.UserAlreadyExists, UserId> {
        return if (isUserExists(UserIdentifier.Username(user.username))) {
            UserError.UserAlreadyExists.left()
        } else {
            queries.insert(
                username = user.username,
                password = user.password,
                email = user.email.orEmpty(),
            ).executeAsOne().right()
        }
    }

    override suspend fun updateUsername(userId: UserId, username: String): Either<UserError.InvalidCredentials, Unit> {
        return try {
            queries.updateUsername(
                userId = userId,
                username = username,
            )
            Unit.right()
        } catch (exception: Exception) {
            UserError.InvalidCredentials.left()
        }
    }

    override suspend fun updateEmail(userId: UserId, email: String): Either<UserError.InvalidCredentials, Unit> {
        return try {
            queries.updateEmail(
                userId = userId,
                email = email,
            )
            Unit.right()
        } catch (exception: Exception) {
            UserError.InvalidCredentials.left()
        }
    }

    override suspend fun isUserExists(identifier: UserIdentifier): Boolean {
        return getUser(identifier) != null
    }

    override suspend fun removeUser(userId: UserId): Boolean {
        return try {
            queries.removeById(userId)
            true
        } catch (exception: Exception) {
            false
        }
    }

    override suspend fun getUsers(): List<User> {
        return queries.getUsers()
            .executeAsList()
            .map(Users::toDomainModel)
    }
}
