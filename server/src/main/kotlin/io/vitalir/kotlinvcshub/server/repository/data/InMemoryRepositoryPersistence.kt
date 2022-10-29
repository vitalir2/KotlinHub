package io.vitalir.kotlinvcshub.server.repository.data

import io.vitalir.kotlinvcshub.server.repository.domain.model.Repository
import io.vitalir.kotlinvcshub.server.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinvcshub.server.user.domain.model.UserId

internal class InMemoryRepositoryPersistence : RepositoryPersistence {

    private val repositories: MutableList<Repository> = mutableListOf()

    override suspend fun isRepositoryExists(userId: UserId, name: String): Boolean {
        return repositories.firstOrNull { it.ownerId == userId && it.name == name } != null
    }

    override suspend fun addRepository(repository: Repository) {
        repositories.add(repository)
    }
}
