package io.vitalir.server.kgit.git

import io.vitalir.server.kgit.client.KGitHttpClient
import io.vitalir.server.kgit.client.Response

internal class GitAuthManagerImpl(
    private val httpClient: KGitHttpClient,
) : GitAuthManager {

    override suspend fun hasAccess(
        repositoryName: String,
        username: String?,
    ): Boolean {
        val response = httpClient.get(
            path = "git/http/auth/",
            body = GitAuthRequest(
                repositoryName = repositoryName,
                username = username,
            )
        )
        return response.code == Response.HttpCode.OK
    }
}
