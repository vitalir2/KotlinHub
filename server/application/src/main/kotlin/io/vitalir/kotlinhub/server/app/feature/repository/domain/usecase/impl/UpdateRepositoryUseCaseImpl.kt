package io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl

import arrow.core.left
import arrow.core.right
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

    // TODO user / repository checking used many times in app - is it a time to make a validator for it?
    //  Like RepositoryIdentifierChecker
    override suspend fun invoke(
        userIdentifier: UserIdentifier,
        repositoryName: String,
        updateRepositoryData: UpdateRepositoryData,
    ): UpdateRepositoryResult {
        if (userPersistence.isUserExists(userIdentifier).not()) {
            return UpdateRepositoryUseCase.Error.UserDoesNotExist(userIdentifier).left()
        }
        if (repositoryPersistence.isRepositoryExists(userIdentifier, repositoryName).not()) {
            return UpdateRepositoryUseCase.Error.RepositoryDoesNotExist(userIdentifier, repositoryName).left()
        }

        repositoryPersistence.updateRepository(
            userIdentifier = userIdentifier,
            repositoryName = repositoryName,
            updateRepositoryData = updateRepositoryData,
        )
        return Unit.right()
    }
}
