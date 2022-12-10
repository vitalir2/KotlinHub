package io.vitalir.kotlinhub.server.app.feature.repository.routes.get

import io.vitalir.kotlinhub.shared.feature.repository.ApiRepository
import kotlinx.serialization.Serializable

@Serializable
internal data class GetRepositoryResponse(
    val repository: ApiRepository,
)
