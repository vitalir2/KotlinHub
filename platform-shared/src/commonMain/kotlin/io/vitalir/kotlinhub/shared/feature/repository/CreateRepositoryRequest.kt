package io.vitalir.kotlinhub.shared.feature.repository

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@Serializable
@JsExport
class CreateRepositoryRequest(
    val name: String,
    val accessMode: ApiRepository.AccessMode,
    val description: String? = null,
)
