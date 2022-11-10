package io.vitalir.kotlinhub.server.app.repository.domain.model

object RepositoryError {

    sealed interface Create {

        object InvalidUserId : Create

        object RepositoryAlreadyExists : Create
    }

    sealed interface Get {

        object InvalidUserLogin : Get

        object RepositoryDoesNotExist : Get
    }
}
