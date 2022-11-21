package io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.rightIfNotNull
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.GetRepositoriesForUserUseCase
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence
import io.vitalir.kotlinhub.shared.feature.user.UserId

internal class GetRepositoriesForUserUseCaseImpl(
    private val userPersistence: UserPersistence,
    private val repositoryPersistence: RepositoryPersistence,
) : GetRepositoriesForUserUseCase {

    override suspend fun invoke(
        currentUserId: UserId?,
        userIdentifier: UserIdentifier,
    ): Either<GetRepositoriesForUserUseCase.Error, List<Repository>> {
        return either {
            val user = userPersistence.getUser(userIdentifier).rightIfNotNull {
                GetRepositoriesForUserUseCase.Error.UserDoesNotExist(userIdentifier)
            }.bind()
            val accessibleRepositories = if (currentUserId == user.id) {
                listOf(Repository.AccessMode.PUBLIC, Repository.AccessMode.PRIVATE)
            } else {
                listOf(Repository.AccessMode.PUBLIC)
            }
            repositoryPersistence.getRepositories(userIdentifier, accessibleRepositories)
        }
    }
}
