package io.vitalir.kotlinhub.server.app.feature.repository.routes.create

import io.vitalir.kotlinhub.shared.feature.repository.ApiRepository
import kotlinx.serialization.Serializable

@Serializable
internal data class CreateRepositoryRequest(
    val name: String,
    val accessMode: ApiRepository.AccessMode,
    val description: String? = null,
)
