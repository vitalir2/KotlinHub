package io.vitalir.kotlinhub.server.app.feature.repository.domain.model.error

import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryIdentifier

data class RepositoryDoesNotExist(val repositoryIdentifier: RepositoryIdentifier) : RepositoryError
