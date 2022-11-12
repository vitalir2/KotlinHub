package io.vitalir.kotlinhub.server.app.feature.user.routes

import io.ktor.http.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.ErrorResponse
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.jwtAuth
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserError
import io.vitalir.kotlinhub.server.app.feature.user.routes.getuser.getUserByLoginRoute
import io.vitalir.kotlinhub.server.app.feature.user.routes.login.loginRoute
import io.vitalir.kotlinhub.server.app.feature.user.routes.registration.registerUserRoute
import io.vitalir.kotlinhub.server.app.feature.user.routes.removeuser.removeUser
import io.vitalir.kotlinhub.server.app.feature.user.routes.updateuser.updateUser
import io.vitalir.kotlinhub.server.app.infrastructure.config.AppConfig
import io.vitalir.kotlinhub.server.app.infrastructure.di.AppGraph

internal fun Routing.userRoutes(
    jwtConfig: AppConfig.Jwt,
    userGraph: AppGraph.UserGraph,
) {
    route("users/") {
        unauthorizedUserRoutes(jwtConfig, userGraph)

        authIndependentUserRoutes(userGraph)

        authorizedUserRoutes(userGraph)
    }
}

private fun Route.unauthorizedUserRoutes(
    jwtConfig: AppConfig.Jwt,
    userGraph: AppGraph.UserGraph,
) {
    registerUserRoute(userGraph.registerUserUseCase)
    loginRoute(jwtConfig, userGraph.loginUseCase)
}

private fun Route.authIndependentUserRoutes(
    userGraph: AppGraph.UserGraph,
) {
    getUserByLoginRoute(userGraph.getUserByIdentifierUseCase)
}

private fun Route.authorizedUserRoutes(
    userGraph: AppGraph.UserGraph,
) {
    jwtAuth {
        updateUser(userGraph.updateUserUseCase)
        removeUser(userGraph.removeUserUseCase)
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
