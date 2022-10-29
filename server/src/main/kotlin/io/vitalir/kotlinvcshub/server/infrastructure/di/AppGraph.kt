package io.vitalir.kotlinvcshub.server.infrastructure.di

import io.vitalir.kotlinvcshub.server.infrastructure.config.AppConfig
import io.vitalir.kotlinvcshub.server.repository.domain.usecase.CreateRepositoryUseCase
import io.vitalir.kotlinvcshub.server.user.domain.persistence.UserPersistence
import io.vitalir.kotlinvcshub.server.user.domain.usecase.GetUserByLoginUseCase
import io.vitalir.kotlinvcshub.server.user.domain.usecase.LoginUseCase
import io.vitalir.kotlinvcshub.server.user.domain.usecase.RegisterUserUseCase

class AppGraph(
    val appConfig: AppConfig,
    val user: UserGraph,
    val repository: RepositoryGraph,
) {

    class UserGraph(
        val userPersistence: UserPersistence,
        val loginUseCase: LoginUseCase,
        val registerUserUseCase: RegisterUserUseCase,
        val getUserByLoginUseCase: GetUserByLoginUseCase,
    )

    class RepositoryGraph(
        val createRepositoryUseCase: CreateRepositoryUseCase,
    )
}
