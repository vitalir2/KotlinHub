package io.vitalir.kotlinhub.shared.feature.repository

import io.vitalir.kotlinhub.shared.common.KMPLocalDateTime
import io.vitalir.kotlinhub.shared.feature.user.UserId
import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@Serializable
@JsExport
data class ApiRepository(
    val ownerId: UserId,
    val name: String,
    val accessMode: AccessMode,
    val createdAt: KMPLocalDateTime,
    val description: String?,
    val httpUrl: String,
) {

    enum class AccessMode {
        PRIVATE,
        PUBLIC,
    }
}

