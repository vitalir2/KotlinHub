package io.vitalir.kotlinvcshub.server.infrastructure.di

import io.vitalir.kotlinvcshub.server.infrastructure.config.AppConfig
import io.vitalir.kotlinvcshub.server.infrastructure.database.createMainSqlDelightDatabase
import io.vitalir.kotlinvcshub.server.infrastructure.database.sqldelight.MainSqlDelight
import io.vitalir.kotlinvcshub.server.user.data.BCryptPasswordManager
import io.vitalir.kotlinvcshub.server.user.data.SqlDelightUserPersistence
import io.vitalir.kotlinvcshub.server.user.domain.persistence.UserPersistence
import io.vitalir.kotlinvcshub.server.user.domain.usecase.impl.GetUserByLoginUseCaseImpl
import io.vitalir.kotlinvcshub.server.user.domain.usecase.impl.LoginUseCaseImpl
import io.vitalir.kotlinvcshub.server.user.domain.usecase.impl.RegisterUserUseCaseImpl
import io.vitalir.kotlinvcshub.server.user.domain.validation.IdentifierValidationRule

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
            registerUserUseCase = RegisterUserUseCaseImpl(
                identifierValidationRule = IdentifierValidationRule,
                userPersistence = userPersistence,
                passwordManager = passwordManager,
            ),
            getUserByLoginUseCase = GetUserByLoginUseCaseImpl(
                userPersistence = userPersistence,
            ),
        )
    }
}
