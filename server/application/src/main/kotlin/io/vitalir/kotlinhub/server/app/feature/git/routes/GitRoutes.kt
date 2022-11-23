package io.vitalir.kotlinhub.server.app.feature.git.routes

import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.infrastructure.di.AppGraph


internal fun Routing.gitRoutes(
    appGraph: AppGraph,
) {
    route("git/") {
        httpBaseAuth(
            hasUserAccessToRepositoryUseCase = appGraph.repository.hasUserAccessToRepositoryUseCase,
            updateRepositoryUseCase = appGraph.repository.updateRepositoryUseCase,
            headerManager = appGraph.network.baseAuthHeaderManager,
        )
    }
}
