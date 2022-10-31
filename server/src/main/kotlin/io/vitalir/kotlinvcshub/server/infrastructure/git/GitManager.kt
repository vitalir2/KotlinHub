package io.vitalir.kotlinvcshub.server.infrastructure.git

import io.vitalir.kotlinvcshub.server.repository.domain.model.Repository

interface GitManager {

    suspend fun initRepository(repository: Repository)
}
