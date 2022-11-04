package io.vitalir.kotlinhub.server.app.user.routes.getuser

import io.vitalir.kotlinhub.server.app.user.routes.common.PureUser
import kotlinx.serialization.Serializable

@Serializable
internal data class GetUserByLoginResponse(
    val user: PureUser,
)
