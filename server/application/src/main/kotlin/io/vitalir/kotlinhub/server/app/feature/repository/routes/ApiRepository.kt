package io.vitalir.kotlinhub.server.app.feature.repository.routes

import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.shared.common.network.ServicesInfo
import io.vitalir.kotlinhub.shared.feature.repository.ApiRepository


internal fun Repository.toApiModel(): ApiRepository {
    return ApiRepository(
        ownerId = owner.id,
        name = name,
        accessMode = accessMode.toApiModel(),
        createdAt = ,
        description = description,
        httpUrl = createHttpResourceUrl(ServicesInfo.ReverseProxy.mainUrl).toString(),
    )
}
