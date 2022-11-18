package io.vitalir.kotlinhub.server.app.feature.repository.domain.model

import io.vitalir.kotlinhub.shared.feature.user.UserId

data class CreateRepositoryData(
    val ownerId: UserId,
    val name: String,
    val accessMode: Repository.AccessMode,
    val description: String? = null,
)
