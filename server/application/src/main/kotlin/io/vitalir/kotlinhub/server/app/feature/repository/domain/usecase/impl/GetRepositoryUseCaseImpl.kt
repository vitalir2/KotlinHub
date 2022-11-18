package io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl

import arrow.core.continuations.either
import arrow.core.rightIfNotNull
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.GetRepositoryResult
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.GetRepositoryUseCase
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence

internal class GetRepositoryUseCaseImpl(
    private val repositoryPersistence: RepositoryPersistence,
    private val userPersistence: UserPersistence,
) : GetRepositoryUseCase {

    override suspend fun invoke(
        userIdentifier: UserIdentifier,
        repositoryName: String,
    ): GetRepositoryResult {
        return either {
            userPersistence.getUser(userIdentifier).rightIfNotNull {
                GetRepositoryUseCase.Error.UserDoesNotExist(userIdentifier)
            }.bind()
            repositoryPersistence.getRepository(userIdentifier, repositoryName).rightIfNotNull {
                GetRepositoryUseCase.Error.RepositoryDoesNotExist(userIdentifier, repositoryName)
            }.bind()
        }
    }
}
