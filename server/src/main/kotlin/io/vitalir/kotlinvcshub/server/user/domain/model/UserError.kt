package io.vitalir.kotlinvcshub.server.user.domain.model

sealed interface UserError {

    object ValidationFailed : UserError

    object UserAlreadyExists : UserError

    object InvalidCredentials : UserError
}
