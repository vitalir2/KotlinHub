package io.vitalir.kotlinhub.shared.feature.git

import io.vitalir.kotlinhub.shared.feature.repository.ApiRepository
import kotlinx.serialization.Serializable

@Serializable
data class GitAuthRequest(
    val userId: Int,
    val repositoryName: String,
    val credentials: String?,
)

data class a(
    val r: ApiRepository,
)
