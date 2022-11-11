package io.vitalir.server.kgit.infrastructure.git

import io.vitalir.kotlinhub.shared.feature.git.GitAuthRequest
import io.vitalir.server.kgit.infrastructure.client.KGitHttpClient
import io.vitalir.server.kgit.infrastructure.client.Response
import io.vitalir.server.kgit.infrastructure.client.Uri

internal class GitAuthManagerImpl(
    private val httpClient: KGitHttpClient,
) : GitAuthManager {

    override suspend fun hasAccess(
        repositoryName: String,
        username: String,
        credentials: String?,
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
                credentials = credentials,
            ),
        )
        return response.code == Response.HttpCode.Valid.OK
    }
}
