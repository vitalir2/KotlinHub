package io.vitalir.kotlinhub.shared.feature.repository

import io.vitalir.kotlinhub.shared.feature.user.UserId
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class ApiRepository(
    val ownerId: UserId,
    val name: String,
    val accessMode: AccessMode,
    val createdAt: LocalDateTime,
    val description: String?,
    val httpUrl: String,
) {

    enum class AccessMode {
        PRIVATE,
        PUBLIC,
    }
}

