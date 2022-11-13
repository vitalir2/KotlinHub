package io.vitalir.kotlinhub.server.app.infrastructure.git

import arrow.core.Either
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository

interface GitManager {

    suspend fun initRepository(repository: Repository): Either<Error, Unit>

    sealed interface Error {
        object Unknown : Error
        data class RepositoryAlreadyExists(val repository: Repository) : Error
    }
}
