package io.vitalir.kotlinvcshub.server.repository.domain.model

import io.vitalir.kotlinvcshub.server.user.domain.model.UserId

data class CreateRepositoryData(
    val ownerId: UserId,
    val name: String,
    val accessMode: Repository.AccessMode,
    val description: String? = null,
)
