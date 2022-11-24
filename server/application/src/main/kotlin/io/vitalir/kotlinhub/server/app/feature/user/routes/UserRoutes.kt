package io.vitalir.kotlinhub.server.app.feature.user.routes

import io.ktor.http.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.jwtAuth
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserError
import io.vitalir.kotlinhub.server.app.feature.user.routes.common.usersTag
import io.vitalir.kotlinhub.server.app.feature.user.routes.get.getUsersDocs
import io.vitalir.kotlinhub.server.app.feature.user.routes.get.getUsersRoute
import io.vitalir.kotlinhub.server.app.feature.user.routes.get.userByIdentifierRoute
import io.vitalir.kotlinhub.server.app.feature.user.routes.login.authRoute
import io.vitalir.kotlinhub.server.app.feature.user.routes.registration.registerDocs
import io.vitalir.kotlinhub.server.app.feature.user.routes.registration.registerUserRoute
import io.vitalir.kotlinhub.server.app.feature.user.routes.removeuser.removeCurrentUser
import io.vitalir.kotlinhub.server.app.feature.user.routes.removeuser.removeCurrentUserDocs
import io.vitalir.kotlinhub.server.app.feature.user.routes.update.updateCurrentUser
import io.vitalir.kotlinhub.server.app.feature.user.routes.update.updateCurrentUserDocs
import io.vitalir.kotlinhub.server.app.infrastructure.config.AppConfig
import io.vitalir.kotlinhub.server.app.infrastructure.di.AppGraph
import io.vitalir.kotlinhub.server.app.infrastructure.docs.kompendiumDocs

internal fun Route.userRoutes(
    jwtConfig: AppConfig.Auth.Jwt,
    userGraph: AppGraph.UserGraph,
) {
    route("users/") {
        kompendiumDocs {
            usersTag()
            registerDocs()
            getUsersDocs()
        }
        registerUserRoute(userGraph.registerUserUseCase)
        getUsersRoute(userGraph.getUsersUseCase)

        authRoute(jwtConfig, userGraph.loginUseCase)
        userByIdentifierRoute(userGraph.getUserByIdentifierUseCase)

        authenticatedRoutes(userGraph)
    }
}

private fun Route.authenticatedRoutes(
    userGraph: AppGraph.UserGraph,
) {
    jwtAuth {
        kompendiumDocs {
            usersTag()
            updateCurrentUserDocs()
            removeCurrentUserDocs()
        }
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
