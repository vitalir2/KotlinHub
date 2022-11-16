package io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl

import arrow.core.continuations.either
import arrow.core.rightIfNotNull
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.UpdateRepositoryData
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.UpdateRepositoryResult
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.UpdateRepositoryUseCase
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence

internal class UpdateRepositoryUseCaseImpl(
    private val userPersistence: UserPersistence,
    private val repositoryPersistence: RepositoryPersistence,
) : UpdateRepositoryUseCase {

    override suspend fun invoke(
        userIdentifier: UserIdentifier,
        repositoryName: String,
        updateRepositoryData: UpdateRepositoryData,
    ): UpdateRepositoryResult {
        return either {
            userPersistence.getUser(userIdentifier).rightIfNotNull {
                UpdateRepositoryUseCase.Error.UserDoesNotExist(userIdentifier)
            }.bind()
            repositoryPersistence.getRepository(userIdentifier, repositoryName).rightIfNotNull {
                UpdateRepositoryUseCase.Error.RepositoryDoesNotExist(userIdentifier, repositoryName)
            }.bind()
            repositoryPersistence.updateRepository(
                userIdentifier = userIdentifier,
                repositoryName = repositoryName,
                updateRepositoryData = updateRepositoryData,
            )
        }
    }
}
