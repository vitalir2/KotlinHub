package io.vitalir.kotlinhub.shared.feature.git

import kotlinx.serialization.Serializable

@Serializable
data class GitAuthRequest(
    val repositoryName: String,
    val username: String,
    val credentials: String?,
)
