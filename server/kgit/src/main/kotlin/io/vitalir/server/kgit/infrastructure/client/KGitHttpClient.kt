package io.vitalir.server.kgit.infrastructure.client

interface KGitHttpClient {

    @WorkerThread
    suspend fun post(
        uri: Uri,
        body: Any,
    ): Response
}
