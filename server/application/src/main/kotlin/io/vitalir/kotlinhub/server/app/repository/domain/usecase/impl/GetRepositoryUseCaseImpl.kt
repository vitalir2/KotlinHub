package io.vitalir.kotlinhub.server.app.repository.domain.usecase.impl

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.vitalir.kotlinhub.server.app.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.repository.domain.model.RepositoryError
import io.vitalir.kotlinhub.server.app.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinhub.server.app.repository.domain.usecase.GetRepositoryUseCase
import io.vitalir.kotlinhub.server.app.user.domain.model.UserCredentials
import io.vitalir.kotlinhub.server.app.user.domain.persistence.UserPersistence

internal class GetRepositoryUseCaseImpl(
    private val repositoryPersistence: RepositoryPersistence,
    private val userPersistence: UserPersistence,
) : GetRepositoryUseCase {

    override suspend fun invoke(userName: String, repositoryName: String): Either<RepositoryError.Get, Repository> {
        if (userPersistence.isUserExists(UserCredentials.Identifier.Login(userName)).not()) {
            return RepositoryError.Get.InvalidUserLogin.left()
        }
        val repository = repositoryPersistence.getRepository(
            userName, repositoryName
        )
        return repository?.right() ?: RepositoryError.Get.RepositoryDoesNotExist.left()
    }

}
