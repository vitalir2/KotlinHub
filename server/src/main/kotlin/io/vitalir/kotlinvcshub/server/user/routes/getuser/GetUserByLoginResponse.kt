package io.vitalir.kotlinvcshub.server.user.routes.getuser

import io.vitalir.kotlinvcshub.server.user.routes.common.PureUser

internal data class GetUserByLoginResponse(
    val user: PureUser,
)
