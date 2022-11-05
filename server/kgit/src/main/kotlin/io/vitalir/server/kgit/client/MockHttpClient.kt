package io.vitalir.server.kgit.client

internal class MockHttpClient(
    private val onGet: (path: String, body: Any?) -> Response,
) : KGitHttpClient {

    override suspend fun get(path: String, body: Any?): Response {
        return onGet(path, body)
    }
}
