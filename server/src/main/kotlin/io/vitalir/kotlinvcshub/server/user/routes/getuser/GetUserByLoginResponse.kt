package io.vitalir.kotlinvcshub.server.user.routes.getuser

import io.vitalir.kotlinvcshub.server.user.routes.common.PureUser
import kotlinx.serialization.Serializable

@Serializable
internal data class GetUserByLoginResponse(
    val user: PureUser,
)
