package io.vitalir.kotlinhub.server.app.feature.user.routes.getuser

import io.vitalir.kotlinhub.server.app.feature.user.routes.common.PureUser
import kotlinx.serialization.Serializable

@Serializable
internal data class GetUserByLoginResponse(
    val user: PureUser,
)
