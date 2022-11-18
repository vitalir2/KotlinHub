package io.vitalir.kotlinhub.server.app.feature.repository.domain.model

import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier

sealed interface RepositoryError {

    interface Unknown : RepositoryError

    interface UserDoesNotExist : RepositoryError {

        val userIdentifier: UserIdentifier
    }

    interface RepositoryDoesNotExist : RepositoryError {

        val userIdentifier: UserIdentifier

        val repositoryName: String
    }
}
