package io.vitalir.server.kgit.git

import io.vitalir.server.kgit.client.WorkerThread

interface GitAuthManager {

    @WorkerThread
    suspend fun hasAccess(
        repositoryName: String,
        username: String,
        credentials: String?,
    ): Boolean
}
