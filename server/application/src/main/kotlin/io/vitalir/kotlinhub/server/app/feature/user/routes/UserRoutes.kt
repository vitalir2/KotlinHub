package io.vitalir.kotlinhub.server.app.feature.user.routes

import io.bkbn.kompendium.core.plugin.NotarizedRoute
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.jwtAuth
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserError
import io.vitalir.kotlinhub.server.app.feature.user.routes.get.getUserByUsernameRoute
import io.vitalir.kotlinhub.server.app.feature.user.routes.get.getUsersRoute
import io.vitalir.kotlinhub.server.app.feature.user.routes.login.loginRoute
import io.vitalir.kotlinhub.server.app.feature.user.routes.registration.registerDocs
import io.vitalir.kotlinhub.server.app.feature.user.routes.registration.registerUserRoute
import io.vitalir.kotlinhub.server.app.feature.user.routes.removeuser.removeCurrentUser
import io.vitalir.kotlinhub.server.app.feature.user.routes.update.updateCurrentUser
import io.vitalir.kotlinhub.server.app.infrastructure.config.AppConfig
import io.vitalir.kotlinhub.server.app.infrastructure.di.AppGraph

internal fun Routing.userRoutes(
    jwtConfig: AppConfig.Jwt,
    userGraph: AppGraph.UserGraph,
) {
    route("users/") {
        userDocs()

        unauthorizedUserRoutes(jwtConfig, userGraph)

        authIndependentUserRoutes(userGraph)

        authorizedUserRoutes(userGraph)
    }
}

private fun Route.userDocs() {
    install(NotarizedRoute()) {
        tags = setOf("user")
        registerDocs()
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
    getUserByUsernameRoute(userGraph.getUserByIdentifierUseCase)
    getUsersRoute(userGraph.getUsersUseCase)
}

private fun Route.authorizedUserRoutes(
    userGraph: AppGraph.UserGraph,
) {
    jwtAuth {
        updateCurrentUser(userGraph.updateUserUseCase)
        removeCurrentUser(userGraph.removeUserUseCase)
    }
}

internal fun getErrorResponseData(userError: UserError): ResponseData {
    return when (userError) {
        is UserError.InvalidCredentials -> {
            ResponseData.fromErrorData(
                code = HttpStatusCode.BadRequest,
                errorMessage = "invalid credentials",
            )
        }

        is UserError.ValidationFailed -> {
            ResponseData.fromErrorData(
                code = HttpStatusCode.BadRequest,
                errorMessage = "invalid credentials format",
            )
        }

        is UserError.UserAlreadyExists -> {
            ResponseData.fromErrorData(
                code = HttpStatusCode.BadRequest,
                errorMessage = "user already exists",
            )
        }
    }
}
