package io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.rightIfNotNull
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.GetRepositoriesForUserUseCase
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence

internal class GetRepositoriesForUserUseCaseImpl(
    private val userPersistence: UserPersistence,
    private val repositoryPersistence: RepositoryPersistence,
) : GetRepositoriesForUserUseCase {

    override suspend fun invoke(userIdentifier: UserIdentifier): Either<GetRepositoriesForUserUseCase.Error, List<Repository>> {
        return either {
            userPersistence.getUser(userIdentifier).rightIfNotNull {
                GetRepositoriesForUserUseCase.Error.UserDoesNotExist(userIdentifier)
            }.bind()
            repositoryPersistence.getRepositories(userIdentifier)
        }
    }
}
