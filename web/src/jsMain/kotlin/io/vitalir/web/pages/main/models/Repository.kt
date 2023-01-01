package io.vitalir.web.pages.main.models

import io.vitalir.kotlinhub.shared.feature.repository.ApiRepository

data class Repository(
    val name: String,
    val accessMode: String,
    val description: String?,
    val updatedAt: String,
)

internal fun ApiRepository.toWebModel(): Repository {
    return Repository(
        name = name,
        accessMode = accessMode.name,
        description = description,
        updatedAt = createdAt.toString(), // TODO
    )
}
