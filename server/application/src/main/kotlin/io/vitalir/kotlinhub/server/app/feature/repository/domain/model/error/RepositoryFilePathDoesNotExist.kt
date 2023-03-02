package io.vitalir.kotlinhub.server.app.feature.repository.domain.model.error

data class RepositoryFilePathDoesNotExist(val absolutePath: String) : RepositoryError
