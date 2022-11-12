package io.vitalir.kotlinhub.server.app.feature.repository.routes.getrepository

import io.vitalir.kotlinhub.server.app.feature.repository.routes.ApiRepository
import kotlinx.serialization.Serializable

@Serializable
data class GetRepositoryResponse(
    val repository: ApiRepository,
)
