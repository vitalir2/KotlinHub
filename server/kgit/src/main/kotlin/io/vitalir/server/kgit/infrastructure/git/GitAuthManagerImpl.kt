package io.vitalir.server.kgit.infrastructure.git

import io.vitalir.kotlinhub.shared.common.network.Path
import io.vitalir.kotlinhub.shared.common.network.ServicesInfo
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
        val url = ServicesInfo.App.mainUrl.copy(
            path = Path("git", "http", "auth"),
        )
        val response = httpClient.post(
            url = url,
            body = GitAuthRequest(
                userId = userId,
                repositoryName = repositoryName,
                credentials = credentials,
            ),
        )
        return response.code == Response.HttpCode.Valid.OK
    }
}
