package io.vitalir.kotlinvcshub.server.infrastructure.di

import io.vitalir.kotlinvcshub.server.user.domain.usecase.LoginUseCase
import io.vitalir.kotlinvcshub.server.user.domain.usecase.RegisterUserUseCase
import io.vitalir.kotlinvcshub.server.user.domain.usecase.ValidateUserCredentialsUseCase

class ApplicationGraph(
    val userGraph: User,
) {

    class User(
        val loginUseCase: LoginUseCase,
        val registerUserUseCase: RegisterUserUseCase,
        val validateUserCredentialsUseCase: ValidateUserCredentialsUseCase,
    )
}
