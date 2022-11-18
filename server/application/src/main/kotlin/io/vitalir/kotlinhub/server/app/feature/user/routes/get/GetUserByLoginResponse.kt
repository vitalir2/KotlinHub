package io.vitalir.kotlinhub.server.app.feature.user.routes.get

import io.vitalir.kotlinhub.server.app.feature.user.routes.common.PureUser
import kotlinx.serialization.Serializable

@Serializable
internal data class GetUserByLoginResponse(
    val user: PureUser,
)
