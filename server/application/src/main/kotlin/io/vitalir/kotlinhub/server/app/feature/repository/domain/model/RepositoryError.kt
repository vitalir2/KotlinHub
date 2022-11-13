package io.vitalir.kotlinhub.server.app.feature.repository.domain.model

import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserId
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier

object RepositoryError {

    // TODO move to use case classes
    sealed interface Create {
        object Unknown : Create

        data class UserDoesNotExist(
            override val userIdentifier: UserIdentifier,
        ) : Create, Common.UserDoesNotExist

        data class RepositoryAlreadyExists(
            val userId: UserId,
            val repositoryName: String,
        ) : Create
    }

    sealed interface Get {

        data class UserDoesNotExist(
            override val userIdentifier: UserIdentifier,
        ) : Get, Common.UserDoesNotExist

        data class RepositoryDoesNotExist(
            override val userIdentifier: UserIdentifier,
            override val repositoryName: String,
        ) : Get, Common.RepositoryDoesNotExist
    }

    sealed interface Common {

        interface UserDoesNotExist : Common {

            val userIdentifier: UserIdentifier
        }

        interface RepositoryDoesNotExist : Common {

            val userIdentifier: UserIdentifier

            val repositoryName: String
        }
    }
}
