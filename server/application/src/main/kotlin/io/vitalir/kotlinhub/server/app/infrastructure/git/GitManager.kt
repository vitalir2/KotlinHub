package io.vitalir.kotlinhub.server.app.infrastructure.git

import arrow.core.Either
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.shared.feature.user.UserId
import java.nio.file.Path

interface GitManager {

    suspend fun initRepository(repository: Repository): Either<Error, Unit>

    fun getRepositoryFiles(
        userId: UserId,
        repositoryName: String,
    ): List<Path>

    suspend fun removeRepositoryByName(
        userId: UserId,
        repositoryName: String,
    ): Boolean

    sealed interface Error {
        object Unknown : Error
        data class RepositoryAlreadyExists(val repository: Repository) : Error
    }
}
