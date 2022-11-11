package io.vitalir.kotlinhub.server.app.feature.user.routes.updateuser

import arrow.core.Either
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.AuthVariant
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.extensions.respondWith
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.UpdateUserUseCase
import io.vitalir.kotlinhub.server.app.feature.user.routes.getErrorResponseData
import io.vitalir.kotlinhub.server.app.infrastructure.auth.userId

internal fun Route.updateUser(
    updateUserUseCase: UpdateUserUseCase,
) {
    authenticate(AuthVariant.JWT.authName) {
        put {
            val userId = call.userId ?: run {
                call.respondWith(ResponseData.unauthorized())
                return@put
            }
            val request = call.receiveNullable<UpdateUserRequest>() ?: run {
                call.respondWith(ResponseData.emptyBody())
                return@put
            }

            val result = updateUserUseCase(
                userId = userId,
                username = request.username,
                email = request.email,
            )

            when (result) {
                is Either.Left -> {
                    val responseData = getErrorResponseData(result.value)
                    call.respondWith(responseData)
                }
                is Either.Right -> {
                    call.respondWith(
                        ResponseData(
                            code = HttpStatusCode.OK,
                        )
                    )
                }
            }
        }
    }
}
