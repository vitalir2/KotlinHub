package io.vitalir.kotlinhub.server.app.user.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.core.rightIfNotNull
import io.vitalir.kotlinhub.server.app.infrastructure.database.sqldelight.MainSqlDelight
import io.vitalir.kotlinhub.server.app.user.data.extensions.toDomainModel
import io.vitalir.kotlinhub.server.app.user.domain.model.User
import io.vitalir.kotlinhub.server.app.user.domain.model.UserCredentials
import io.vitalir.kotlinhub.server.app.user.domain.model.UserError
import io.vitalir.kotlinhub.server.app.user.domain.model.UserId
import io.vitalir.kotlinhub.server.app.user.domain.persistence.UserPersistence
import io.vitalir.kotlinvschub.server.infrastructure.database.sqldelight.CUsersQueries

internal class SqlDelightUserPersistence(
    private val sqlDelightDatabase: MainSqlDelight,
) : UserPersistence {

    private val queries: CUsersQueries
        get() = sqlDelightDatabase.cUsersQueries

    override suspend fun getUser(identifier: UserCredentials.Identifier): Either<UserError.InvalidCredentials, User> {
        return when (identifier) {
            is UserCredentials.Identifier.Email -> queries.getByEmail(identifier.value)
            is UserCredentials.Identifier.Login -> queries.getByLogin(identifier.value)
        }
            .executeAsOneOrNull()
            ?.toDomainModel()
            .rightIfNotNull { UserError.InvalidCredentials }
    }

    override suspend fun getUser(userId: UserId): User? {
        return queries.getById(userId)
            .executeAsOneOrNull()
            ?.toDomainModel()
    }

    override suspend fun addUser(user: User): Either<UserError.UserAlreadyExists, Unit> {
        return if (isUserExists(UserCredentials.Identifier.Login(user.login))) {
            UserError.UserAlreadyExists.left()
        } else {
            queries.insert(
                login = user.login,
                password = user.password,
                email = user.email.orEmpty(),
            ).right()
        }
    }

    override suspend fun isUserExists(identifier: UserCredentials.Identifier): Boolean {
        val existingUser = getUser(identifier).orNull()
        return existingUser != null
    }

    // TODO
    override suspend fun isUserExists(userId: UserId): Boolean {
        return true
    }
}
