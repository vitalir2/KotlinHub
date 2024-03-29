package io.vitalir.kotlinhub.server.app.feature.user.routes.common.extensions

import io.ktor.http.*
import io.ktor.server.plugins.*
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier

// TODO handle errors the right way
internal fun Parameters.userId(currentUserId: Int?): UserIdentifier.Id {
    return userIdOrNull(currentUserId)
        ?: throw BadRequestException("Invalid format for userId=${this["userId"]}")
}

internal fun Parameters.userIdOrNull(currentUserId: Int?): UserIdentifier.Id? {
    val pathUserId = this["userId"]
    return when (pathUserId) {
        "current" -> currentUserId
        else -> pathUserId?.toIntOrNull()
    }?.let(UserIdentifier::Id)
}
