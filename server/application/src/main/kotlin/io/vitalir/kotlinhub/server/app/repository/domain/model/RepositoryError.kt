package io.vitalir.kotlinhub.server.app.repository.domain.model

object RepositoryError {

    sealed interface Create {

        object InvalidUserId : Create

        object RepositoryAlreadyExists : Create
    }
}
