package io.vitalir.kotlinhub.server.app.repository.domain.usecase.impl

import arrow.core.left
import arrow.core.rightIfNotNull
import io.vitalir.kotlinhub.server.app.repository.domain.model.RepositoryError
import io.vitalir.kotlinhub.server.app.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinhub.server.app.repository.domain.usecase.GetRepositoryResult
import io.vitalir.kotlinhub.server.app.repository.domain.usecase.GetRepositoryUseCase
import io.vitalir.kotlinhub.server.app.user.domain.model.UserCredentials
import io.vitalir.kotlinhub.server.app.user.domain.persistence.UserPersistence

internal class GetRepositoryUseCaseImpl(
    private val repositoryPersistence: RepositoryPersistence,
    private val userPersistence: UserPersistence,
) : GetRepositoryUseCase {

    override suspend fun invoke(username: String, repositoryName: String): GetRepositoryResult {
        val identifier = UserCredentials.Identifier.Login(username)
        return when {
            userPersistence.isUserExists(identifier).not() -> {
                RepositoryError.Get.InvalidUserLogin.left()
            }
            else -> {
                val repository = repositoryPersistence.getRepository(username, repositoryName)
                repository.rightIfNotNull { RepositoryError.Get.RepositoryDoesNotExist }
            }
        }
    }

}
