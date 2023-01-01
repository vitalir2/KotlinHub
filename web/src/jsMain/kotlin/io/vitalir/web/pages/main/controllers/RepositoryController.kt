package io.vitalir.web.pages.main.controllers

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.client.call.*
import io.vitalir.kotlinhub.shared.feature.repository.ApiRepository
import io.vitalir.kotlinhub.shared.feature.repository.GetRepositoriesResponse
import io.vitalir.kotlinhub.shared.feature.user.UserId
import io.vitalir.web.pages.main.models.Repository
import io.vitalir.web.pages.main.models.toWebModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RepositoryController(private val httpClient: HttpClient) {

    private val innerState = MutableStateFlow<State>(State.Loading)
    val state = innerState.asStateFlow()

    suspend fun refreshRepositories(userId: UserId) {
        val response = httpClient.get("/repository/$userId") {
            contentType(ContentType.Application.Json)
        }

        console.log("Refresh repositories response=$response")

        if (response.status.isSuccess()) {
            val result = response.body<GetRepositoriesResponse>()
            innerState.value = State.Loaded(repositories = result.repositories.map(ApiRepository::toWebModel))
        } else {
            innerState.value = State.Error("Cannot fetch repositories")
        }
    }

    sealed interface State {

        object Loading : State

        data class Loaded(
            val repositories: List<Repository> = emptyList(),
        ) : State

        data class Error(
            val text: String,
        ) : State
    }
}
