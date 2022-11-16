package io.vitalir.kotlinhub.server.app.feature.repository.routes.create

import kotlinx.serialization.Serializable

@Serializable
internal data class CreateRepositoryResponse(
    val repositoryUrl: String,
)
