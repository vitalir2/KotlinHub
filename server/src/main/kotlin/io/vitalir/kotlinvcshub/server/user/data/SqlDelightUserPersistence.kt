package io.vitalir.kotlinvcshub.server.user.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.core.rightIfNotNull
import io.vitalir.kotlinvcshub.server.infrastructure.database.sqldelight.MainSqlDelight
import io.vitalir.kotlinvcshub.server.user.data.extensions.toDomainModel
import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvcshub.server.user.domain.model.UserError
import io.vitalir.kotlinvcshub.server.user.domain.persistence.UserPersistence
import io.vitalir.kotlinvschub.server.infrastructure.database.sqldelight.UsersQueries

internal class SqlDelightUserPersistence(
    private val sqlDelightDatabase: MainSqlDelight,
) : UserPersistence {

    private val queries: UsersQueries
        get() = sqlDelightDatabase.usersQueries

    override suspend fun getUser(identifier: User.Credentials.Identifier): Either<UserError.InvalidCredentials, User> {
        return when (identifier) {
            is User.Credentials.Identifier.Email -> queries.getByEmail(identifier.value)
            is User.Credentials.Identifier.Login -> queries.getByLogin(identifier.value)
        }
            .executeAsOneOrNull()
            ?.toDomainModel()
            .rightIfNotNull { UserError.InvalidCredentials }
    }

    override suspend fun addUser(user: User): Either<UserError.UserAlreadyExists, Unit> {
        val existingUser = queries.getById(user.id).executeAsOneOrNull()
        return if (existingUser != null) {
            UserError.UserAlreadyExists.left()
        } else {
            queries.insert(
                login = user.login,
                password = user.password,
                email = user.email.orEmpty(),
            ).right()
        }
    }
}
