package io.vitalir.kotlinvcshub.server.infrastructure.di

import arrow.core.Either
import arrow.core.left
import io.vitalir.kotlinvcshub.server.infrastructure.config.AppConfig
import io.vitalir.kotlinvcshub.server.infrastructure.database.createMainSqlDelightDatabase
import io.vitalir.kotlinvcshub.server.infrastructure.database.sqldelight.MainSqlDelight
import io.vitalir.kotlinvcshub.server.user.data.SqlDelightUserPersistence
import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvcshub.server.user.domain.model.UserError
import io.vitalir.kotlinvcshub.server.user.data.BCryptPasswordManager
import io.vitalir.kotlinvcshub.server.user.domain.persistence.UserPersistence
import io.vitalir.kotlinvcshub.server.user.domain.usecase.RegisterUserUseCase
import io.vitalir.kotlinvcshub.server.user.domain.usecase.impl.LoginUseCaseImpl

internal class AppGraphFactoryImpl : AppGraphFactory {

    override fun create(
        appConfig: AppConfig,
    ): AppGraph {
        val database = createMainSqlDelightDatabase(appConfig.database)
        return AppGraph(
            appConfig = appConfig,
            user = createUserGraph(database),
        )
    }

    private fun createUserGraph(
        database: MainSqlDelight,
    ): AppGraph.User {
        val userPersistence: UserPersistence = SqlDelightUserPersistence(database)
        val passwordManager = BCryptPasswordManager()
        return AppGraph.User(
            loginUseCase = LoginUseCaseImpl(
                userPersistence = userPersistence,
                passwordManager = passwordManager,
            ),
            registerUserUseCase = object : RegisterUserUseCase {
                override fun invoke(credentials: User.Credentials): Either<UserError, User> {
                    return UserError.UserAlreadyExists.left() // TODO
                }
            },
        )
    }
}
