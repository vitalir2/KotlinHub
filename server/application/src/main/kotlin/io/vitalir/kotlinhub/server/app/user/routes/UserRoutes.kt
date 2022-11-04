package io.vitalir.kotlinhub.server.app.user.routes

import io.ktor.http.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.ErrorResponse
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.infrastructure.config.AppConfig
import io.vitalir.kotlinhub.server.app.infrastructure.di.AppGraph
import io.vitalir.kotlinhub.server.app.user.domain.model.UserError
import io.vitalir.kotlinhub.server.app.user.routes.getuser.getUserByLoginRoute
import io.vitalir.kotlinhub.server.app.user.routes.login.loginRoute
import io.vitalir.kotlinhub.server.app.user.routes.registration.registerUserRoute

internal fun Routing.userRoutes(
    jwtConfig: AppConfig.Jwt,
    userGraph: AppGraph.UserGraph,
) {
    route("users/") {
        registerUserRoute(userGraph.registerUserUseCase)
        loginRoute(jwtConfig, userGraph.loginUseCase)
        getUserByLoginRoute(userGraph.getUserByLoginUseCase)
    }
}

internal fun getErrorResponseData(userError: UserError): ResponseData {
    return when (userError) {
        is UserError.InvalidCredentials -> {
            ResponseData(
                code = HttpStatusCode.BadRequest,
                body = ErrorResponse(
                    code = HttpStatusCode.BadRequest.value,
                    message = "invalid credentials",
                )
            )
        }
        is UserError.ValidationFailed -> {
            ResponseData(
                code = HttpStatusCode.BadRequest,
                body = ErrorResponse(
                    code = HttpStatusCode.BadRequest.value,
                    message = "invalid credentials format",
                )
            )
        }
        is UserError.UserAlreadyExists -> {
            val responseBody = ErrorResponse(
                code = HttpStatusCode.BadRequest.value,
                message = "user already exists",
            )
            ResponseData(code = HttpStatusCode.BadRequest, body = responseBody)
        }
    }
}

internal const val HOUR_MS = 1000 * 60 * 60
