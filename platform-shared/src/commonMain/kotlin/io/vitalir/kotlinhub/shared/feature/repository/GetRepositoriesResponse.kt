package io.vitalir.kotlinhub.shared.feature.repository

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@Serializable
@JsExport
class GetRepositoriesResponse(
    val repositories: Array<ApiRepository>,
)
