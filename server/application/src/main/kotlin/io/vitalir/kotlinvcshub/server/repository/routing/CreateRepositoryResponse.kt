package io.vitalir.kotlinvcshub.server.repository.routing

import kotlinx.serialization.Serializable

@Serializable
internal data class CreateRepositoryResponse(
    val repositoryUrl: String,
)
