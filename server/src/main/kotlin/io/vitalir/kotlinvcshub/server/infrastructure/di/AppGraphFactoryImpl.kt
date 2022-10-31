package io.vitalir.kotlinvcshub.server.infrastructure.di

import io.vitalir.kotlinvcshub.server.common.data.JavaLocalDateTimeProvider
import io.vitalir.kotlinvcshub.server.infrastructure.config.AppConfig
import io.vitalir.kotlinvcshub.server.infrastructure.database.createMainSqlDelightDatabase
import io.vitalir.kotlinvcshub.server.infrastructure.database.sqldelight.MainSqlDelight
import io.vitalir.kotlinvcshub.server.infrastructure.git.GitManagerImpl
import io.vitalir.kotlinvcshub.server.repository.data.SqlDelightRepositoryPersistence
import io.vitalir.kotlinvcshub.server.repository.domain.usecase.impl.CreateRepositoryUseCaseImpl
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
        val userGraph = createUserGraph(database)
        return AppGraph(
            appConfig = appConfig,
            user = userGraph,
            repository = createRepositoryGraph(
                userPersistence = userGraph.userPersistence,
                database = database,
            ),
        )
    }

    private fun createUserGraph(
        database: MainSqlDelight,
    ): AppGraph.UserGraph {
        val userPersistence: UserPersistence = SqlDelightUserPersistence(database)
        val passwordManager = BCryptPasswordManager()
        return AppGraph.UserGraph(
            userPersistence = userPersistence,
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

    private fun createRepositoryGraph(
        userPersistence: UserPersistence,
        database: MainSqlDelight,
    ): AppGraph.RepositoryGraph {
        return AppGraph.RepositoryGraph(
            createRepositoryUseCase = CreateRepositoryUseCaseImpl(
                userPersistence = userPersistence,
                repositoryPersistence = SqlDelightRepositoryPersistence(
                    mainDatabase = database,
                ),
                localDateTimeProvider = JavaLocalDateTimeProvider(),
                gitManager = GitManagerImpl(
                    userPersistence = userPersistence, // TODO add user entity in User model to remove it
                ),
            )
        )
    }
}
