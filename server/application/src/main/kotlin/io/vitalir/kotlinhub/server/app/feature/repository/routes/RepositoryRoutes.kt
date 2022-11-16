package io.vitalir.kotlinhub.server.app.feature.repository.routes

import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.jwtAuth
import io.vitalir.kotlinhub.server.app.feature.repository.routes.create.createRepositoryRoute
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
        jwtAuth {
            removeRepositoryForCurrentUserRoute(repositoryGraph.removeRepositoryUseCase)
            updateRepositoryForCurrentUserRoute(repositoryGraph.updateRepositoryUseCase)
        }
    }
}
