package io.vitalir.kotlinvcshub.server.user.domain

sealed interface LoginError {

    object InvalidCredentialsFormat : LoginError

    object InvalidCredentials : LoginError
}
