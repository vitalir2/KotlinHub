package io.vitalir.kotlinhub.shared.feature.git

import kotlinx.serialization.Serializable

@Serializable
data class GitAuthRequest(
    val userId: Int,
    val repositoryName: String,
    val credentials: String?,
)
