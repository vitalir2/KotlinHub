package io.vitalir.kotlinhub.server.app.feature.git.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.repository.domain.usecase.GetRepositoryUseCase
import io.vitalir.kotlinhub.server.app.repository.domain.usecase.impl.GetRepositoryUseCaseImpl
import io.vitalir.kotlinhub.shared.feature.git.GitAuthRequest


internal fun Routing.gitRoutes() {
    route("git/") {
        httpBaseAuth(
            getRepositoryUseCase = GetRepositoryUseCaseImpl(),
        )
    }
}

private fun Route.httpBaseAuth(
    getRepositoryUseCase: GetRepositoryUseCase,
) {
    post("http/auth/") {
        val request = call.receive<GitAuthRequest>()
        val repository = getRepositoryUseCase(
            userName = request.username,
            repositoryName = request.repositoryName,
        )
        when (repository.accessMode) {
            // TODO Permit any call for now
            Repository.AccessMode.PUBLIC -> {
                application.log.info("APP: Public repository")
                call.respond(HttpStatusCode.OK)
            }
            Repository.AccessMode.PRIVATE -> {
                application.log.info("APP: Private repository")
                if (isCredentialsValid(request.credentials)) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.Forbidden)
                }
            }
        }
    }
}

// TODO check credentials by some domain obj
private fun isCredentialsValid(credentials: String?): Boolean {
    return credentials != null
}
