package io.vitalir.kotlinhub.server.app.feature.repository.routes.update

import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import kotlinx.serialization.Serializable

@Serializable
data class UpdateRepositoryRequest(
    val accessMode: Repository.AccessMode? = null,
)
