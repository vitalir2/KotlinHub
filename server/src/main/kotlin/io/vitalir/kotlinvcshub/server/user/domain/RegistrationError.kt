package io.vitalir.kotlinvcshub.server.user.domain

sealed interface RegistrationError {

    object InvalidCredentialsFormat : RegistrationError

    object UserAlreadyExists : RegistrationError
}
