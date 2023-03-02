package io.vitalir.kotlinhub.server.app.infrastructure.di

import io.ktor.server.application.*
import io.vitalir.kotlinhub.server.app.common.data.JavaLocalDateTimeProvider
import io.vitalir.kotlinhub.server.app.feature.repository.data.FileSystemRepositoryTreePersistence
import io.vitalir.kotlinhub.server.app.feature.repository.data.RepositoryIdentifierConverter
import io.vitalir.kotlinhub.server.app.feature.repository.data.SqlDelightRepositoryPersistence
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl.CreateRepositoryUseCaseImpl
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl.GetRepositoriesForUserUseCaseImpl
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl.GetRepositoryDirFilesUseCaseImpl
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl.GetRepositoryUseCaseImpl
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl.HasUserAccessToRepositoryUseCaseImpl
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl.RemoveRepositoryUseCaseImpl
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl.UpdateRepositoryUseCaseImpl
import io.vitalir.kotlinhub.server.app.feature.user.data.SqlDelightUserPersistence
import io.vitalir.kotlinhub.server.app.feature.user.data.UserIdentifierConverter
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.impl.GetUserByIdentifierUseCaseImpl
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.impl.GetUsersUseCaseImpl
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.impl.LoginUseCaseImpl
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.impl.RegisterUserUseCaseImpl
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.impl.RemoveUserUseCaseImpl
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.impl.UpdateUserUseCaseImpl
import io.vitalir.kotlinhub.server.app.feature.user.domain.validation.IdentifierValidationRule
import io.vitalir.kotlinhub.server.app.infrastructure.auth.impl.BCryptPasswordManager
import io.vitalir.kotlinhub.server.app.infrastructure.config.AppConfig
import io.vitalir.kotlinhub.server.app.infrastructure.database.createMainSqlDelightDatabase
import io.vitalir.kotlinhub.server.app.infrastructure.database.sqldelight.MainSqlDelight
import io.vitalir.kotlinhub.server.app.infrastructure.encoding.impl.KtorBase64Manager
import io.vitalir.kotlinhub.server.app.infrastructure.git.GitManagerImpl
import io.vitalir.kotlinhub.server.app.infrastructure.logging.impl.KtorLogger
import io.vitalir.kotlinhub.server.app.infrastructure.routing.impl.BaseAuthHeaderManager

internal class AppGraphFactoryImpl(
    private val application: Application,
) : AppGraphFactory {

    override fun create(
        appConfig: AppConfig,
    ): AppGraph {
        val utilsGraph = createUtilsGraph()
        val database = createMainSqlDelightDatabase(appConfig.database)
        val authGraph = createAuthGraph()
        val networkGraph = createNetworkGraph(utilsGraph)
        val userGraph = createUserGraph(database, authGraph)
        return AppGraph(
            appConfig = appConfig,
            user = userGraph,
            repository = createRepositoryGraph(
                userGraph = userGraph,
                database = database,
                repositoryConfig = appConfig.repository,
                authGraph = authGraph,
            ),
            auth = authGraph,
            network = networkGraph,
            utils = utilsGraph,
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
            getUsersUseCase = GetUsersUseCaseImpl(
                userPersistence = userPersistence,
            ),
        )
    }

    private fun createRepositoryGraph(
        repositoryConfig: AppConfig.Repository,
        userGraph: AppGraph.UserGraph,
        database: MainSqlDelight,
        authGraph: AppGraph.AuthGraph,
    ): AppGraph.RepositoryGraph {
        val userPersistence = userGraph.userPersistence
        val localDateTimeProvider = JavaLocalDateTimeProvider()
        val userIdentifierConverter = UserIdentifierConverter(database)
        val repositoryIdentifierConverter = RepositoryIdentifierConverter(database, userIdentifierConverter)
        val repositoryPersistence = SqlDelightRepositoryPersistence(
            mainDatabase = database,
            userIdentifierConverter = userIdentifierConverter,
            localDateTimeProvider = localDateTimeProvider,
        )
        val gitManager = GitManagerImpl(
            repositoryConfig = repositoryConfig,
        )
        val repositoryTreePersistence = FileSystemRepositoryTreePersistence(
            gitManager = gitManager,
            identifierConverter = repositoryIdentifierConverter,
        )
        return AppGraph.RepositoryGraph(
            createRepositoryUseCase = CreateRepositoryUseCaseImpl(
                userPersistence = userPersistence,
                repositoryPersistence = repositoryPersistence,
                localDateTimeProvider = localDateTimeProvider,
                gitManager = gitManager,
            ),
            getRepositoryUseCase = GetRepositoryUseCaseImpl(
                userPersistence = userPersistence,
                repositoryPersistence = repositoryPersistence,
            ),
            getRepositoriesForUserUseCase = GetRepositoriesForUserUseCaseImpl(
                userPersistence = userPersistence,
                repositoryPersistence = repositoryPersistence,
            ),
            removeRepositoryUseCase = RemoveRepositoryUseCaseImpl(
                userPersistence = userPersistence,
                repositoryPersistence = repositoryPersistence,
                gitManager = gitManager,
            ),
            updateRepositoryUseCase = UpdateRepositoryUseCaseImpl(
                userPersistence = userPersistence,
                repositoryPersistence = repositoryPersistence,
            ),
            hasUserAccessToRepositoryUseCase = HasUserAccessToRepositoryUseCaseImpl(
                userPersistence = userPersistence,
                repositoryPersistence = repositoryPersistence,
                passwordManager = authGraph.passwordManager,
                userIdentifierValidationRule = IdentifierValidationRule,
            ),
            getRepositoryDirFilesUseCase = GetRepositoryDirFilesUseCaseImpl(
                repositoryPersistence = repositoryPersistence,
                repositoryTreePersistence = repositoryTreePersistence,
            ),
        )
    }

    private fun createUtilsGraph(): AppGraph.UtilsGraph {
        return AppGraph.UtilsGraph(
            logger = KtorLogger(application.log),
            base64Manager = KtorBase64Manager(),
        )
    }

    private fun createNetworkGraph(
        utilsGraph: AppGraph.UtilsGraph,
    ): AppGraph.NetworkGraph {
        return AppGraph.NetworkGraph(
            baseAuthHeaderManager = BaseAuthHeaderManager(
                base64Manager = utilsGraph.base64Manager,
            ),
        )
    }

    private fun createAuthGraph(): AppGraph.AuthGraph {
        val passwordManager = BCryptPasswordManager()
        return AppGraph.AuthGraph(
            passwordManager = passwordManager,
        )
    }
}
