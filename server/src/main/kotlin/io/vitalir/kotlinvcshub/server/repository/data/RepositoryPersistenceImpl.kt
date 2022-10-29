package io.vitalir.kotlinvcshub.server.repository.data

import io.vitalir.kotlinvcshub.server.repository.domain.model.Repository
import io.vitalir.kotlinvcshub.server.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinvcshub.server.user.domain.model.UserId

// TODO
internal class RepositoryPersistenceImpl : RepositoryPersistence {
    override suspend fun isRepositoryExists(userId: UserId, name: String): Boolean {
        return false
    }

    override suspend fun addRepository(repository: Repository) {

    }
}
