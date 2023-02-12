package io.vitalir.kotlinhub.shared.feature.repository

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@Serializable
@JsExport
data class GetRepositoryResponse(
    val repository: ApiRepository,
)
