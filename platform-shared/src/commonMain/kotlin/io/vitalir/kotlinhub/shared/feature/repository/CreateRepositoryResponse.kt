package io.vitalir.kotlinhub.shared.feature.repository

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@Serializable
@JsExport
class CreateRepositoryResponse(
    val repositoryUrl: String,
)
