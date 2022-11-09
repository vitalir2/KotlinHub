package io.vitalir.kotlinhub.server.app.infrastructure.di

import io.vitalir.kotlinhub.server.app.infrastructure.config.AppConfig
import io.vitalir.kotlinhub.server.app.repository.domain.usecase.CreateRepositoryUseCase
import io.vitalir.kotlinhub.server.app.repository.domain.usecase.GetRepositoryUseCase
import io.vitalir.kotlinhub.server.app.user.domain.persistence.UserPersistence
import io.vitalir.kotlinhub.server.app.user.domain.usecase.GetUserByLoginUseCase
import io.vitalir.kotlinhub.server.app.user.domain.usecase.LoginUseCase
import io.vitalir.kotlinhub.server.app.user.domain.usecase.RegisterUserUseCase

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
        val getRepositoryUseCase: GetRepositoryUseCase,
    )
}
