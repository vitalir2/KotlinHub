package io.vitalir.server.kgit.infrastructure.client

import io.vitalir.kotlinhub.shared.common.network.Url

interface KGitHttpClient {

    @WorkerThread
    suspend fun post(
        url: Url,
        body: Any,
    ): Response
}
