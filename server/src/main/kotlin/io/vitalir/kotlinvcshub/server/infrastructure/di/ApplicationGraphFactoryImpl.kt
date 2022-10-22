package io.vitalir.kotlinvcshub.server.infrastructure.di

import arrow.core.Either
import arrow.core.left
import io.vitalir.kotlinvcshub.server.infrastructure.config.AppConfig
import io.vitalir.kotlinvcshub.server.infrastructure.database.createMainSqlDelightDatabase
import io.vitalir.kotlinvcshub.server.infrastructure.database.sqldelight.MainSqlDelight
import io.vitalir.kotlinvcshub.server.user.data.SqlDelightUserPersistence
import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvcshub.server.user.domain.model.UserError
import io.vitalir.kotlinvcshub.server.user.domain.persistence.UserPersistence
import io.vitalir.kotlinvcshub.server.user.domain.usecase.RegisterUserUseCase
import io.vitalir.kotlinvcshub.server.user.domain.usecase.ValidateUserCredentialsUseCase
import io.vitalir.kotlinvcshub.server.user.domain.usecase.impl.LoginUseCaseImpl
import io.vitalir.kotlinvcshub.server.user.domain.usecase.impl.ValidateUserCredentialsUseCaseImpl

internal class ApplicationGraphFactoryImpl : ApplicationGraphFactory {

    override fun create(
        appConfig: AppConfig,
    ): ApplicationGraph {
        val database = createMainSqlDelightDatabase(appConfig.database)
        return ApplicationGraph(
            userGraph = createUserGraph(database),
        )
    }

    private fun createUserGraph(
        database: MainSqlDelight,
    ): ApplicationGraph.User {
        val userPersistence: UserPersistence = SqlDelightUserPersistence(database)
        val validateUserCredentialsUseCase: ValidateUserCredentialsUseCase = ValidateUserCredentialsUseCaseImpl()
        return ApplicationGraph.User(
            loginUseCase = LoginUseCaseImpl(userPersistence, validateUserCredentialsUseCase),
            registerUserUseCase = object : RegisterUserUseCase {
                override fun invoke(credentials: User.Credentials): Either<UserError, User> {
                    return UserError.UserAlreadyExists.left() // TODO
                }
            },
            validateUserCredentialsUseCase = validateUserCredentialsUseCase,
        )
    }
}
