package io.vitalir.server.kgit.infrastructure.git

import io.vitalir.server.kgit.infrastructure.client.WorkerThread

interface GitAuthManager {

    @WorkerThread
    suspend fun hasAccess(
        repositoryName: String,
        username: String,
        credentials: String?,
    ): Boolean
}
