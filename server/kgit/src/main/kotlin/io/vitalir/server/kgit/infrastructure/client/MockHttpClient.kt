package io.vitalir.server.kgit.infrastructure.client

import io.vitalir.kotlinhub.shared.common.network.Url

internal class MockHttpClient(
    private val onPost: (url: Url, body: Any) -> Response,
) : KGitHttpClient {

    override suspend fun post(
        url: Url,
        body: Any,
    ): Response {
        return onPost(url, body)
    }
}
