package io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.rightIfNotNull
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryIdentifier
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.error.RepositoryDoesNotExist
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.GetRepositoryResult
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.GetRepositoryUseCase
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence
import io.vitalir.kotlinhub.shared.feature.user.UserId

internal class GetRepositoryUseCaseImpl(
    private val repositoryPersistence: RepositoryPersistence,
    private val userPersistence: UserPersistence,
) : GetRepositoryUseCase {

    override suspend fun invoke(
        userIdentifier: UserIdentifier,
        repositoryName: String,
        currentUserId: UserId?,
    ): GetRepositoryResult {
        return either {
            val repositoryOwner = getUserOrError(userIdentifier).bind()
            repositoryPersistence.getRepository(userIdentifier, repositoryName)
                .takeUnless { it?.isPrivate == true && repositoryOwner.id != currentUserId }
                .rightIfNotNull {
                GetRepositoryUseCase.Error.RepositoryDoesNotExist(userIdentifier, repositoryName)
            }.bind()
        }
    }

    override suspend fun invoke(repositoryIdentifier: RepositoryIdentifier): Either<io.vitalir.kotlinhub.server.app.feature.repository.domain.model.error.RepositoryError, Repository> {
        return repositoryPersistence.getRepository(repositoryIdentifier)
            .rightIfNotNull {
                RepositoryDoesNotExist(repositoryIdentifier)
            }
    }

    private suspend fun getUserOrError(
        userIdentifier: UserIdentifier,
    ): Either<GetRepositoryUseCase.Error.UserDoesNotExist, User> {
        return userPersistence.getUser(userIdentifier).rightIfNotNull {
            GetRepositoryUseCase.Error.UserDoesNotExist(userIdentifier)
        }
    }
}
