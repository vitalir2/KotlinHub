package io.vitalir.web.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.vitalir.kotlinhub.shared.feature.repository.ApiRepository
import io.vitalir.kotlinhub.shared.feature.repository.GetRepositoriesResponse
import io.vitalir.kotlinhub.shared.feature.user.UserId
import io.vitalir.web.common.NetworkConstants
import io.vitalir.web.common.NetworkException
import io.vitalir.web.pages.main.models.Repository
import io.vitalir.web.pages.main.models.toWebModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class RepositoriesRepositoryImpl(
    private val httpClient: HttpClient,
) : RepositoriesRepository {

    override suspend fun get(userId: UserId): Result<List<Repository>> = withContext(Dispatchers.Default) {
        val response = try {
            val result = httpClient.get(NetworkConstants.BASE_PATH + "/repositories/$userId/")
            // TODO remove logs for production
            console.log("Get repositories response=$result")
            result
        } catch (exception: Throwable) {
            console.log("Caught exception: $exception")
            return@withContext Result.failure(NetworkException)
        }


        if (response.status.isSuccess()) {
            val responseBody = response.body<GetRepositoriesResponse>()
            val repositories = responseBody.repositories.map(ApiRepository::toWebModel)
            Result.success(repositories)
        } else {
            Result.failure(NetworkException)
        }
    }
}
