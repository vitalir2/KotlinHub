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
import io.vitalir.kotlinhub.server.app.infrastructure.auth.userId

internal fun Route.updateUser(
    updateUserUseCase: UpdateUserUseCase,
) {
    authenticate(AuthVariant.JWT.authName) {
        put {
            val userId = call.userId
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

private fun getErrorResponseData(error: UpdateUserUseCase.Error): ResponseData {
    return when (error) {
        is UpdateUserUseCase.Error.InvalidArguments -> {
            ResponseData.fromErrorData(
                code = HttpStatusCode.BadRequest,
                errorMessage = error.message,
            )
        }
        is UpdateUserUseCase.Error.NoUser -> {
            ResponseData.fromErrorData(
                code = HttpStatusCode.BadRequest,
                errorMessage = "no user with id=${error.userId}"
            )
        }
    }
}
