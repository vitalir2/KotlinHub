package io.vitalir.kotlinhub.server.app.feature.repository.routes

import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.jwtAuth
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryError
import io.vitalir.kotlinhub.server.app.feature.repository.routes.create.createRepositoryRoute
import io.vitalir.kotlinhub.server.app.feature.repository.routes.get.getRepositoriesByUserId
import io.vitalir.kotlinhub.server.app.feature.repository.routes.get.getRepositoryRoute
import io.vitalir.kotlinhub.server.app.feature.repository.routes.remove.removeRepositoryForCurrentUserRoute
import io.vitalir.kotlinhub.server.app.feature.repository.routes.update.updateRepositoryForCurrentUserRoute
import io.vitalir.kotlinhub.server.app.infrastructure.di.AppGraph

internal fun Routing.repositoryRoutes(
    repositoryGraph: AppGraph.RepositoryGraph,
) {
    route("repositories/") {
        createRepositoryRoute(repositoryGraph.createRepositoryUseCase)
        getRepositoryRoute(repositoryGraph.getRepositoryUseCase)
        getRepositoriesByUserId(repositoryGraph.getRepositoriesForUserUseCase)
        jwtAuth {
            removeRepositoryForCurrentUserRoute(repositoryGraph.removeRepositoryUseCase)
            updateRepositoryForCurrentUserRoute(repositoryGraph.updateRepositoryUseCase)
        }
    }
}

internal fun RepositoryError.toResponseData(): ResponseData {
    return when (this) {
        is RepositoryError.RepositoryDoesNotExist -> ResponseData.badRequest(
            "repository $repositoryName for user $userIdentifier does not exist",
        )
        is RepositoryError.Unknown -> ResponseData.serverError()
        is RepositoryError.UserDoesNotExist -> ResponseData.badRequest(
            message = "user $userIdentifier does not exist",
        )
    }
}
