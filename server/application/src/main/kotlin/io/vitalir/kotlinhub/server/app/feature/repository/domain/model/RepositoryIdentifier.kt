package io.vitalir.kotlinhub.server.app.feature.repository.domain.model

import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier

sealed interface RepositoryIdentifier {

    // TODO support it through the app
    @JvmInline
    value class Id(val value: RepositoryId) : RepositoryIdentifier

    data class OwnerIdentifierAndName(
        val ownerIdentifier: UserIdentifier,
        val repositoryName: String,
    ) : RepositoryIdentifier
}
