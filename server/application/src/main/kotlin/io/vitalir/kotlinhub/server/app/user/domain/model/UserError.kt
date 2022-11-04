package io.vitalir.kotlinhub.server.app.user.domain.model

sealed interface UserError {

    object ValidationFailed : UserError

    object UserAlreadyExists : UserError

    object InvalidCredentials : UserError
}
