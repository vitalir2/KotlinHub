package io.vitalir.kotlinhub.server.app.feature.repository.routes.get

import io.vitalir.kotlinhub.server.app.feature.repository.routes.ApiRepository
import kotlinx.serialization.Serializable

@Serializable
internal data class GetRepositoriesResponse(
    val repositories: List<ApiRepository>,
)
