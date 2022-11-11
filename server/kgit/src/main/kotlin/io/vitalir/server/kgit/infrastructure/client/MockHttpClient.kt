package io.vitalir.server.kgit.infrastructure.client

internal class MockHttpClient(
    private val onPost: (uri: Uri, body: Any) -> Response,
) : KGitHttpClient {

    override suspend fun post(
        uri: Uri,
        body: Any,
    ): Response {
        return onPost(uri, body)
    }
}
