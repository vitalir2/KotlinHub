package io.vitalir.kotlinhub.server.app.repository.routing

import kotlinx.serialization.Serializable

@Serializable
internal data class CreateRepositoryResponse(
    val repositoryUrl: String,
)
