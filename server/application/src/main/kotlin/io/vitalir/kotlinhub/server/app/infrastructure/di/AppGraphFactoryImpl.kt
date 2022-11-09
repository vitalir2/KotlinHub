package io.vitalir.kotlinhub.server.app.infrastructure.di

import io.vitalir.kotlinhub.server.app.common.data.JavaLocalDateTimeProvider
import io.vitalir.kotlinhub.server.app.infrastructure.config.AppConfig
import io.vitalir.kotlinhub.server.app.infrastructure.database.createMainSqlDelightDatabase
import io.vitalir.kotlinhub.server.app.infrastructure.database.sqldelight.MainSqlDelight
import io.vitalir.kotlinhub.server.app.infrastructure.git.GitManagerImpl
import io.vitalir.kotlinhub.server.app.repository.data.SqlDelightRepositoryPersistence
import io.vitalir.kotlinhub.server.app.repository.domain.usecase.impl.CreateRepositoryUseCaseImpl
import io.vitalir.kotlinhub.server.app.repository.domain.usecase.impl.GetRepositoryUseCaseImpl
import io.vitalir.kotlinhub.server.app.user.data.BCryptPasswordManager
import io.vitalir.kotlinhub.server.app.user.data.SqlDelightUserPersistence
import io.vitalir.kotlinhub.server.app.user.domain.persistence.UserPersistence
import io.vitalir.kotlinhub.server.app.user.domain.usecase.impl.GetUserByLoginUseCaseImpl
import io.vitalir.kotlinhub.server.app.user.domain.usecase.impl.LoginUseCaseImpl
import io.vitalir.kotlinhub.server.app.user.domain.usecase.impl.RegisterUserUseCaseImpl
import io.vitalir.kotlinhub.server.app.user.domain.validation.IdentifierValidationRule

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
        val repositoryPersistence = SqlDelightRepositoryPersistence(
            mainDatabase = database,
        )
        return AppGraph.RepositoryGraph(
            createRepositoryUseCase = CreateRepositoryUseCaseImpl(
                userPersistence = userPersistence,
                repositoryPersistence = repositoryPersistence,
                localDateTimeProvider = JavaLocalDateTimeProvider(),
                gitManager = GitManagerImpl(),
            ),
            getRepositoryUseCase = GetRepositoryUseCaseImpl(
                userPersistence = userPersistence,
                repositoryPersistence = repositoryPersistence,
            )
        )
    }
}
