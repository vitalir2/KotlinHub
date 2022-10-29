package io.vitalir.kotlinvcshub.server.repository.domain.model

object RepositoryError {

    sealed interface Create {

        object InvalidUserId : Create

        object RepositoryAlreadyExists : Create
    }

    sealed interface Get {

        object InvalidUserId : Get

        object RepositoryDoesNotExist : Get
    }
}
