package io.vitalir.kotlinhub.server.app.feature.user.routes.common

import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserId
import kotlinx.serialization.Serializable

@Serializable
internal data class PureUser(
    val id: UserId,
    val login: String,
    val firstName: String?,
    val lastName: String?,
    val email: String?,

)
