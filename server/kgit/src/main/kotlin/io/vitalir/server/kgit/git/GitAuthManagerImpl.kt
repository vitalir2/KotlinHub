package io.vitalir.server.kgit.git

import io.vitalir.server.kgit.client.KGitHttpClient
import io.vitalir.server.kgit.client.Response
import io.vitalir.server.kgit.client.Uri

internal class GitAuthManagerImpl(
    private val httpClient: KGitHttpClient,
) : GitAuthManager {

    override suspend fun hasAccess(
        repositoryName: String,
        username: String?,
        service: GitService,
    ): Boolean {
        val uri = Uri(
            protocol = Uri.Protocol.HTTP,
            host = "app",
            port = 8080,
            pathSegments = listOf("git", "http", "auth"),
        )
        val response = httpClient.post(
            uri = uri,
            body = GitAuthRequest(
                repositoryName = repositoryName,
                username = username,
                service = service,
            ),
        )
        return response.code == Response.HttpCode.Valid.OK
    }
}
