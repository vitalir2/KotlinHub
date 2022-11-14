package io.vitalir.kotlinhub.server.app.infrastructure.di

import io.ktor.server.application.*
import io.vitalir.kotlinhub.server.app.common.data.JavaLocalDateTimeProvider
import io.vitalir.kotlinhub.server.app.feature.repository.data.SqlDelightRepositoryPersistence
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl.CreateRepositoryUseCaseImpl
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl.GetRepositoryUseCaseImpl
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl.RemoveRepositoryUseCaseImpl
import io.vitalir.kotlinhub.server.app.feature.user.data.SqlDelightUserPersistence
import io.vitalir.kotlinhub.server.app.infrastructure.auth.impl.BCryptPasswordManager
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.impl.GetUserByIdentifierUseCaseImpl
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.impl.LoginUseCaseImpl
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.impl.RegisterUserUseCaseImpl
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.impl.RemoveUserUseCaseImpl
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.impl.UpdateUserUseCaseImpl
import io.vitalir.kotlinhub.server.app.feature.user.domain.validation.IdentifierValidationRule
import io.vitalir.kotlinhub.server.app.infrastructure.auth.impl.Base64AuthManager
import io.vitalir.kotlinhub.server.app.infrastructure.config.AppConfig
import io.vitalir.kotlinhub.server.app.infrastructure.database.createMainSqlDelightDatabase
import io.vitalir.kotlinhub.server.app.infrastructure.database.sqldelight.MainSqlDelight
import io.vitalir.kotlinhub.server.app.infrastructure.encoding.impl.KtorBase64Manager
import io.vitalir.kotlinhub.server.app.infrastructure.git.GitManagerImpl
import io.vitalir.kotlinhub.server.app.infrastructure.logging.impl.KtorLogger

internal class AppGraphFactoryImpl(
    private val application: Application,
) : AppGraphFactory {

    override fun create(
        appConfig: AppConfig,
    ): AppGraph {
        val logger = KtorLogger(application.log)
        val database = createMainSqlDelightDatabase(appConfig.database)
        val authGraph = createAuthGraph()
        val userGraph = createUserGraph(database, authGraph)
        return AppGraph(
            appConfig = appConfig,
            user = userGraph,
            repository = createRepositoryGraph(
                userPersistence = userGraph.userPersistence,
                database = database,
                repositoryConfig = appConfig.repository,
            ),
            auth = authGraph,
            logger = logger,
        )
    }

    private fun createUserGraph(
        database: MainSqlDelight,
        authGraph: AppGraph.AuthGraph,
    ): AppGraph.UserGraph {
        val userPersistence: UserPersistence = SqlDelightUserPersistence(database)
        return AppGraph.UserGraph(
            userPersistence = userPersistence,
            loginUseCase = LoginUseCaseImpl(
                userPersistence = userPersistence,
                passwordManager = authGraph.passwordManager,
            ),
            registerUserUseCase = RegisterUserUseCaseImpl(
                identifierValidationRule = IdentifierValidationRule,
                userPersistence = userPersistence,
                passwordManager = authGraph.passwordManager,
            ),
            getUserByIdentifierUseCase = GetUserByIdentifierUseCaseImpl(
                userPersistence = userPersistence,
            ),
            updateUserUseCase = UpdateUserUseCaseImpl(
                userPersistence = userPersistence,
                userValidationRule = IdentifierValidationRule,
            ),
            removeUserUseCase = RemoveUserUseCaseImpl(
                userPersistence = userPersistence,
            ),
        )
    }

    private fun createRepositoryGraph(
        repositoryConfig: AppConfig.Repository,
        userPersistence: UserPersistence,
        database: MainSqlDelight,
    ): AppGraph.RepositoryGraph {
        val repositoryPersistence = SqlDelightRepositoryPersistence(
            mainDatabase = database,
        )
        val gitManager = GitManagerImpl(
            repositoryConfig = repositoryConfig,
        )
        return AppGraph.RepositoryGraph(
            createRepositoryUseCase = CreateRepositoryUseCaseImpl(
                userPersistence = userPersistence,
                repositoryPersistence = repositoryPersistence,
                localDateTimeProvider = JavaLocalDateTimeProvider(),
                gitManager = gitManager,
            ),
            getRepositoryUseCase = GetRepositoryUseCaseImpl(
                userPersistence = userPersistence,
                repositoryPersistence = repositoryPersistence,
            ),
            removeRepositoryUseCase = RemoveRepositoryUseCaseImpl(
                userPersistence = userPersistence,
                repositoryPersistence = repositoryPersistence,
                gitManager = gitManager,
            )
        )
    }

    private fun createAuthGraph(): AppGraph.AuthGraph {
        val passwordManager = BCryptPasswordManager()
        return AppGraph.AuthGraph(
            authManager = Base64AuthManager(
                base64Manager = KtorBase64Manager(),
                passwordManager = passwordManager,
            ),
            passwordManager = passwordManager,
        )
    }
}
