package io.vitalir.server.kgit.infrastructure.git

import io.vitalir.kotlinhub.shared.common.network.Path
import io.vitalir.kotlinhub.shared.common.network.Scheme
import io.vitalir.kotlinhub.shared.common.network.Url
import io.vitalir.kotlinhub.shared.feature.git.GitAuthRequest
import io.vitalir.kotlinhub.shared.feature.user.UserId
import io.vitalir.server.kgit.infrastructure.client.KGitHttpClient
import io.vitalir.server.kgit.infrastructure.client.Response

internal class GitAuthManagerImpl(
    private val httpClient: KGitHttpClient,
) : GitAuthManager {

    override suspend fun hasAccess(
        userId: UserId,
        repositoryName: String,
        credentials: String?,
    ): Boolean {
        val uri = Url(
            scheme = Scheme.HTTP,
            host = "app", // TODO make config with these values
            port = 8080,
            path = Path("git", "http", "auth"),
        )
        val response = httpClient.post(
            url = uri,
            body = GitAuthRequest(
                userId = userId,
                repositoryName = repositoryName,
                credentials = credentials,
            ),
        )
        return response.code == Response.HttpCode.Valid.OK
    }
}
