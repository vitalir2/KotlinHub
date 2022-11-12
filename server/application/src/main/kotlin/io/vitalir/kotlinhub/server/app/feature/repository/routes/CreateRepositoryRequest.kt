package io.vitalir.kotlinhub.server.app.feature.repository.routes

import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import kotlinx.serialization.Serializable

@Serializable
internal data class CreateRepositoryRequest(
    val name: String,
    val accessMode: Repository.AccessMode,
    val description: String? = null,
)
