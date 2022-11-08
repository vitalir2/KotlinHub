package io.vitalir.kotlinhub.server.app.repository.domain.usecase.impl

import ch.qos.logback.classic.Logger
import io.vitalir.kotlinhub.server.app.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.repository.domain.usecase.GetRepositoryUseCase
import io.vitalir.kotlinhub.server.app.user.domain.model.User
import io.vitalir.kotlinhub.server.app.user.domain.model.UserCredentials
import io.vitalir.kotlinhub.server.app.user.domain.password.PasswordManager
import java.time.LocalDateTime
import org.slf4j.LoggerFactory

internal class GetRepositoryUseCaseImpl : GetRepositoryUseCase {

    private val logger = LoggerFactory.getLogger(Logger::class.java)

    private val fakePasswordManager: PasswordManager = object : PasswordManager {
        override fun encode(password: String): String {
            return password
        }

        override fun comparePasswords(plaintext: String, hashed: String): Boolean {
            return plaintext == hashed
        }

    }

    // TODO remove fake
    override suspend fun invoke(userName: String, repositoryName: String): Repository {
        val owner = User.fromCredentials(
            credentials = UserCredentials(UserCredentials.Identifier.Login(userName), ""),
            passwordManager = fakePasswordManager,
        )
        val accessMode = Repository.AccessMode.PRIVATE
        return Repository(
            owner = owner,
            name = repositoryName,
            accessMode = accessMode,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
        )
    }
}
