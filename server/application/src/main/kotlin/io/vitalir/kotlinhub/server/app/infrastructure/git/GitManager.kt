package io.vitalir.kotlinhub.server.app.infrastructure.git

import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository

interface GitManager {

    suspend fun initRepository(repository: Repository)
}
