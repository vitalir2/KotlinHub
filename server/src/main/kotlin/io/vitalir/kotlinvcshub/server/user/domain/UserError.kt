package io.vitalir.kotlinvcshub.server.user.domain

sealed interface UserError {

    object InvalidCredentialsFormat : UserError

    object UserAlreadyExists : UserError

    object InvalidCredentials : UserError
}
