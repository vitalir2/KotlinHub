package io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.RemoveRepositoryUseCase
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserId
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence

internal class RemoveRepositoryUseCaseImpl(
    private val userPersistence: UserPersistence,
    private val repositoryPersistence: RepositoryPersistence,
) : RemoveRepositoryUseCase {

    override suspend fun invoke(userId: UserId, repositoryName: String): Either<RemoveRepositoryUseCase.Error, Unit> {
        val userIdentifier = UserIdentifier.Id(userId)
        return when {
            userPersistence.isUserExists(userIdentifier).not() -> {
                RemoveRepositoryUseCase.Error.UserDoesNotExist(userIdentifier).left()
            }
            repositoryPersistence.isRepositoryExists(userId, repositoryName).not() -> {
                RemoveRepositoryUseCase.Error.RepositoryDoesNotExist(userIdentifier, repositoryName).left()
            }
            else -> {
                repositoryPersistence.removeRepositoryByName(userId, repositoryName)
                Unit.right()
            }
        }
    }
}
