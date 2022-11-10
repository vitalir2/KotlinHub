package io.vitalir.server.kgit.client

interface KGitHttpClient {

    @WorkerThread
    suspend fun post(
        uri: Uri,
        body: Any,
    ): Response
}
