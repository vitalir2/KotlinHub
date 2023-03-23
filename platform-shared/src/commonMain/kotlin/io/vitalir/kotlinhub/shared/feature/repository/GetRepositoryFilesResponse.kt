package io.vitalir.kotlinhub.shared.feature.repository

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@Serializable
@JsExport
class GetRepositoryFilesResponse(
    val files: Array<ApiRepositoryFile>,
)

@Serializable
@JsExport
class ApiRepositoryFile(
    val name: String,
    val type: Type,
) {
    enum class Type {
        REGULAR,
        FOLDER,
        UNKNOWN,
    }
}
