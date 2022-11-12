package io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl

import arrow.core.left
import arrow.core.rightIfNotNull
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryError
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.GetRepositoryResult
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.GetRepositoryUseCase
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence

internal class GetRepositoryUseCaseImpl(
    private val repositoryPersistence: RepositoryPersistence,
    private val userPersistence: UserPersistence,
) : GetRepositoryUseCase {

    override suspend fun invoke(username: String, repositoryName: String): GetRepositoryResult {
        val identifier = UserIdentifier.Username(username)
        return when {
            userPersistence.isUserExists(identifier).not() -> {
                RepositoryError.Get.InvalidUsername(username).left()
            }
            else -> {
                val repository = repositoryPersistence.getRepository(username, repositoryName)
                repository.rightIfNotNull { RepositoryError.Get.RepositoryDoesNotExist(username, repositoryName) }
            }
        }
    }

}
