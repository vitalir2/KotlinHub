package io.vitalir.kotlinhub.server.app.infrastructure.git

import io.vitalir.kotlinhub.server.app.repository.domain.model.Repository

interface GitManager {

    suspend fun initRepository(repository: Repository)
}
