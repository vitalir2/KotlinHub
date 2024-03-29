package io.vitalir.kotlinhub.server.app.feature.repository.routes

import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.infrastructure.time.toKMPModel
import io.vitalir.kotlinhub.shared.common.network.ServicesInfo
import io.vitalir.kotlinhub.shared.feature.repository.ApiRepository


internal fun Repository.toApiModel(): ApiRepository {
    return ApiRepository(
        ownerId = owner.id,
        name = name,
        accessMode = accessMode.toApiModel(),
        createdAt = createdAt.toKMPModel(),
        description = description,
        httpUrl = createHttpResourceUrl(ServicesInfo.ReverseProxy.mainUrl).toString(),
    )
}

internal fun Repository.AccessMode.toApiModel(): ApiRepository.AccessMode {
    return when (this) {
        Repository.AccessMode.PUBLIC -> ApiRepository.AccessMode.PUBLIC
        Repository.AccessMode.PRIVATE -> ApiRepository.AccessMode.PRIVATE
    }
}

internal fun ApiRepository.AccessMode.toDomainModel(): Repository.AccessMode {
    return when (this) {
        ApiRepository.AccessMode.PRIVATE -> Repository.AccessMode.PRIVATE
        ApiRepository.AccessMode.PUBLIC -> Repository.AccessMode.PUBLIC
    }
}
