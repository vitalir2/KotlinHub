package io.vitalir.kotlinhub.server.app.infrastructure.auth

import com.auth0.jwt.interfaces.Payload
import io.vitalir.kotlinhub.server.app.user.domain.model.UserId as UserIdValue

sealed class AuthenticationPayload<out T>(
    val name: String,
    val value: T,
) {

    class UserId(
        value: UserIdValue,
    ) : AuthenticationPayload<UserIdValue>(
        name = NAME,
        value = value,
    ) {

        companion object {
            const val NAME = "userId"
        }
    }
}

internal val Payload.userId: UserIdValue?
    get() = getClaim(AuthenticationPayload.UserId.NAME).asInt()
