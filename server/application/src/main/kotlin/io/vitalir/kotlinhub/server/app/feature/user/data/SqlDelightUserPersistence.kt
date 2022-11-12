package io.vitalir.kotlinhub.server.app.feature.user.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.vitalir.kotlinhub.server.app.feature.user.data.extensions.toDomainModel
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserError
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserId
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence
import io.vitalir.kotlinhub.server.app.infrastructure.database.sqldelight.MainSqlDelight
import io.vitalir.kotlinvschub.server.infrastructure.database.sqldelight.CUsersQueries

internal class SqlDelightUserPersistence(
    private val sqlDelightDatabase: MainSqlDelight,
) : UserPersistence {

    private val queries: CUsersQueries
        get() = sqlDelightDatabase.cUsersQueries

    override suspend fun getUser(identifier: UserIdentifier): User? {
        return when (identifier) {
            is UserIdentifier.Email -> queries.getByEmail(identifier.value)
            is UserIdentifier.Id -> queries.getById(identifier.value)
            is UserIdentifier.Login -> queries.getByLogin(identifier.value)
        }
            .executeAsOneOrNull()
            ?.toDomainModel()
    }

    override suspend fun addUser(user: User): Either<UserError.UserAlreadyExists, Unit> {
        return if (isUserExists(UserIdentifier.Login(user.login))) {
            UserError.UserAlreadyExists.left()
        } else {
            queries.insert(
                login = user.login,
                password = user.password,
                email = user.email.orEmpty(),
            ).right()
        }
    }

    override suspend fun updateUsername(userId: UserId, username: String): Either<UserError.InvalidCredentials, Unit> {
        TODO()
    }

    override suspend fun updateEmail(userId: UserId, email: String): Either<UserError.InvalidCredentials, Unit> {
        TODO("Not yet implemented")
    }

    suspend fun updateUser(user: User): Either<UserError.InvalidCredentials, Unit> {
        return if (isUserExists(user.identifier).not()) {
            UserError.InvalidCredentials.left()
        } else {
            queries.update(
                userId = user.id,
                login = user.login,
                email = user.email,
            )
            Unit.right()
        }
    }

    override suspend fun isUserExists(identifier: UserIdentifier): Boolean {
        return getUser(identifier) != null
    }
}
