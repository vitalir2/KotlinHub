package io.vitalir.server.kgit.infrastructure.git

import io.vitalir.kotlinhub.shared.feature.user.UserId
import io.vitalir.server.kgit.infrastructure.client.WorkerThread

interface GitAuthManager {

    @WorkerThread
    suspend fun hasAccess(
        userId: UserId,
        repositoryName: String,
        credentials: String?,
    ): Boolean
}
