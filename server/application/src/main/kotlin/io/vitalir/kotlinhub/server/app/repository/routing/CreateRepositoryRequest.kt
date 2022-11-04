package io.vitalir.kotlinhub.server.app.repository.routing

import io.vitalir.kotlinhub.server.app.repository.domain.model.Repository
import kotlinx.serialization.Serializable

@Serializable
internal data class CreateRepositoryRequest(
    val name: String,
    val accessMode: Repository.AccessMode,
    val description: String? = null,
)
