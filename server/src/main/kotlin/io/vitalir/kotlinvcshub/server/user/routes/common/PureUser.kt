package io.vitalir.kotlinvcshub.server.user.routes.common

import io.vitalir.kotlinvcshub.server.user.domain.model.UserId

internal data class PureUser(
    val id: UserId,
    val login: String,
    val firstName: String?,
    val lastName: String?,
    val email: String?,

)
