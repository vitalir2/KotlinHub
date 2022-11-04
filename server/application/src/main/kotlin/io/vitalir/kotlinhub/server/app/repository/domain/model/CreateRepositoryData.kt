package io.vitalir.kotlinhub.server.app.repository.domain.model

import io.vitalir.kotlinhub.server.app.user.domain.model.UserId

data class CreateRepositoryData(
    val ownerId: UserId,
    val name: String,
    val accessMode: Repository.AccessMode,
    val description: String? = null,
)
