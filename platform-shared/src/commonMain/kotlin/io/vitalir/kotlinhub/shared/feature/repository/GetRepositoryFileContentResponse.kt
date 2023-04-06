package io.vitalir.kotlinhub.shared.feature.repository

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
@Serializable
// TODO use on the backend
class GetRepositoryFileContentResponse(
    val content: ByteArray,
)
