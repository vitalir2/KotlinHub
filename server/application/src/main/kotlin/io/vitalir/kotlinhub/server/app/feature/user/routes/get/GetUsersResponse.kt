package io.vitalir.kotlinhub.server.app.feature.user.routes.get

import io.vitalir.kotlinhub.shared.feature.user.ApiUser
import kotlinx.serialization.Serializable

@Serializable
internal data class GetUsersResponse(
    val users: List<ApiUser>,
)
