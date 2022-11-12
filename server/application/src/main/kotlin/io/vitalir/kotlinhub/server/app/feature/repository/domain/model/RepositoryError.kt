package io.vitalir.kotlinhub.server.app.feature.repository.domain.model

import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserId

object RepositoryError {

    sealed interface Create {

        data class InvalidUserId(val userId: UserId) : Create

        data class RepositoryAlreadyExists(
            val userId: UserId,
            val repositoryName: String,
        ) : Create
    }

    sealed interface Get {

        data class InvalidUsername(val username: String) : Get

        data class RepositoryDoesNotExist(
            val username: String,
            val repositoryName: String,
        ) : Get
    }
}
