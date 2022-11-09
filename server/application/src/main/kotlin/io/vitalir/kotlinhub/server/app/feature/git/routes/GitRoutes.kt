package io.vitalir.kotlinhub.server.app.feature.git.routes

import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.repository.domain.usecase.GetRepositoryUseCase


internal fun Routing.gitRoutes(
    getRepositoryUseCase: GetRepositoryUseCase,
) {
    route("git/") {
        httpBaseAuth(
            getRepositoryUseCase = getRepositoryUseCase,
        )
    }
}
