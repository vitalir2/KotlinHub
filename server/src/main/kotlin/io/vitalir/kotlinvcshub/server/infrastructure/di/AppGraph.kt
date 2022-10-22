package io.vitalir.kotlinvcshub.server.infrastructure.di

import io.vitalir.kotlinvcshub.server.infrastructure.config.AppConfig
import io.vitalir.kotlinvcshub.server.user.domain.usecase.LoginUseCase
import io.vitalir.kotlinvcshub.server.user.domain.usecase.RegisterUserUseCase
import io.vitalir.kotlinvcshub.server.user.domain.usecase.ValidateUserCredentialsUseCase

class AppGraph(
    val appConfig: AppConfig,
    val user: User,
) {

    class User(
        val loginUseCase: LoginUseCase,
        val registerUserUseCase: RegisterUserUseCase,
        val validateUserCredentialsUseCase: ValidateUserCredentialsUseCase,
    )
}
