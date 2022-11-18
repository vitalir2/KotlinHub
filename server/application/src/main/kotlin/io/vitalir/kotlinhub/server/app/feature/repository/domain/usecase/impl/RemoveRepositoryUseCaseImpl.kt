package io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.rightIfNotNull
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.RemoveRepositoryUseCase
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence
import io.vitalir.kotlinhub.server.app.infrastructure.git.GitManager
import io.vitalir.kotlinhub.shared.feature.user.UserId

internal class RemoveRepositoryUseCaseImpl(
    private val userPersistence: UserPersistence,
    private val repositoryPersistence: RepositoryPersistence,
    private val gitManager: GitManager,
) : RemoveRepositoryUseCase {

    override suspend fun invoke(userId: UserId, repositoryName: String): Either<RemoveRepositoryUseCase.Error, Unit> {
        val userIdentifier = UserIdentifier.Id(userId)
        return either {
            userPersistence.getUser(userIdentifier).rightIfNotNull {
                RemoveRepositoryUseCase.Error.UserDoesNotExist(userIdentifier)
            }.bind()
            repositoryPersistence.getRepository(userIdentifier, repositoryName).rightIfNotNull {
                RemoveRepositoryUseCase.Error.RepositoryDoesNotExist(userIdentifier, repositoryName)
            }.bind()
            gitManager.removeRepositoryByName(userId, repositoryName)
                .takeIf { it }
                .rightIfNotNull { RemoveRepositoryUseCase.Error.Unknown }
                .bind()
            repositoryPersistence.removeRepositoryByName(userId, repositoryName)
        }
    }
}
