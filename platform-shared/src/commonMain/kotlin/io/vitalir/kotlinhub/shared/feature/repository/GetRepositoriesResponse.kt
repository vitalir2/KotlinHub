package io.vitalir.kotlinhub.shared.feature.repository

import kotlinx.serialization.Serializable

@Serializable
data class GetRepositoriesResponse(
    val repositories: List<ApiRepository>,
)
