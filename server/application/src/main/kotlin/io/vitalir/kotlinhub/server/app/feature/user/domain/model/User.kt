package io.vitalir.kotlinhub.server.app.feature.user.domain.model

import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.infrastructure.auth.PasswordManager

data class User(
    val id: UserId,
    val login: String,
    val password: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val repositories: List<Repository> = emptyList(),
) {

    val identifier: UserIdentifier = UserIdentifier.Id(id)

    companion object {
        private const val AUTOGENERATED_ID = 0

        fun fromCredentials(
            credentials: UserCredentials,
            passwordManager: PasswordManager,
        ): User {
            val (login, email) = credentials.identifier.userIdentifiers
            return User(
                id = AUTOGENERATED_ID,
                login = login,
                password = passwordManager.encode(credentials.password),
                email = email,
            )
        }

        private data class UserIdentifiers(
            val login: String,
            val email: String? = null,
        )

        private val UserIdentifier.userIdentifiers: UserIdentifiers
            get() = when (this) {
                is UserIdentifier.Email -> UserIdentifiers(
                    login = value,
                    email = value,
                )
                is UserIdentifier.Id -> throw IllegalStateException("Cannot create login and email from user id which does not exist")
                is UserIdentifier.Login -> UserIdentifiers(
                    login = value,
                )
            }
    }
}