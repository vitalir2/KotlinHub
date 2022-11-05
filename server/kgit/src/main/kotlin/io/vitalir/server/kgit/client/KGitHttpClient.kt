package io.vitalir.server.kgit.client

interface KGitHttpClient {

    @WorkerThread
    suspend fun get(
        path: String,
        body: Any? = null,
    ): Response
}
