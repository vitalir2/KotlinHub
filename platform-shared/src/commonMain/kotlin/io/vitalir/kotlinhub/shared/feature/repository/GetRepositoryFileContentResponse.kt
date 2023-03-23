package io.vitalir.kotlinhub.shared.feature.repository

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
@Serializable
class GetRepositoryFileContentResponse(
    val content: ByteArray,
)
