package io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase

import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserCredentials
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier

interface HasUserAccessToRepositoryUseCase {

    suspend operator fun invoke(
        userCredentials: UserCredentials,
        repositoryIdentifier: RepositoryIdentifier,
    ): Boolean

    suspend operator fun invoke(
        userIdentifier: UserIdentifier?,
        repositoryIdentifier: RepositoryIdentifier,
    ): Boolean
}
