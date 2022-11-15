package io.vitalir.kotlinhub.server.app.feature.repository.routes

import io.vitalir.kotlinhub.server.app.common.routes.LocalDateTimeToStringSerializer
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.shared.feature.user.UserId
import java.time.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class ApiRepository(
    val ownerId: UserId,
    val name: String,
    val accessMode: Repository.AccessMode,
    @Serializable(with = LocalDateTimeToStringSerializer::class)
    val createdAt: LocalDateTime,
    @Serializable(with = LocalDateTimeToStringSerializer::class)
    val updatedAt: LocalDateTime,
    val description: String?,
)
