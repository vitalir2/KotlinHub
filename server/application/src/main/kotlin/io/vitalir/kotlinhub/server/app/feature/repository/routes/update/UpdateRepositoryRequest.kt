package io.vitalir.kotlinhub.server.app.feature.repository.routes.update

import io.vitalir.kotlinhub.shared.feature.repository.ApiRepository
import kotlinx.serialization.Serializable

@Serializable
internal data class UpdateRepositoryRequest(
    val accessMode: ApiRepository.AccessMode? = null,
)
