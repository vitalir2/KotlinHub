package io.vitalir.kotlinhub.server.app.feature.git.routes

import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.infrastructure.auth.AuthManager
import io.vitalir.kotlinhub.server.app.repository.domain.usecase.GetRepositoryUseCase


internal fun Routing.gitRoutes(
    getRepositoryUseCase: GetRepositoryUseCase,
    authManager: AuthManager,
) {
    route("git/") {
        httpBaseAuth(
            getRepositoryUseCase = getRepositoryUseCase,
            authManager = authManager,
        )
    }
}
