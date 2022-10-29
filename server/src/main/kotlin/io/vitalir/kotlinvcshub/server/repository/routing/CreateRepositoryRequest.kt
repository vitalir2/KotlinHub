package io.vitalir.kotlinvcshub.server.repository.routing

import io.vitalir.kotlinvcshub.server.repository.domain.model.Repository
import kotlinx.serialization.Serializable

@Serializable
internal data class CreateRepositoryRequest(
    val name: String,
    val accessMode: Repository.AccessMode,
    val description: String? = null,
)
