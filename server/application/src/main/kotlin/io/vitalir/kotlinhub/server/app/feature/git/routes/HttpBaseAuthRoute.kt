package io.vitalir.kotlinhub.server.app.feature.git.routes

import arrow.core.Either
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.extensions.respondWith
import io.vitalir.kotlinhub.server.app.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.repository.domain.model.RepositoryError
import io.vitalir.kotlinhub.server.app.repository.domain.usecase.GetRepositoryResult
import io.vitalir.kotlinhub.server.app.repository.domain.usecase.GetRepositoryUseCase
import io.vitalir.kotlinhub.shared.feature.git.GitAuthRequest

internal fun Route.httpBaseAuth(
    getRepositoryUseCase: GetRepositoryUseCase,
) {
    post("http/auth/") {
        val request = call.receive<GitAuthRequest>()
        val result = getRepositoryUseCase(
            username = request.username,
            repositoryName = request.repositoryName,
        )
        handleGetRepositoryResult(request, result)
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handleGetRepositoryResult(
    request: GitAuthRequest,
    result: GetRepositoryResult,
) {
    when (result) {
        is Either.Left -> {
            call.respondWith(result.value.toErrorResponseData())
        }
        is Either.Right -> {
            handleRepositoryFromResult(request, result.value)
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handleRepositoryFromResult(
    request: GitAuthRequest,
    repository: Repository,
) {
    when (repository.accessMode) {
        // Permit any call for now
        Repository.AccessMode.PUBLIC -> {
            call.respond(HttpStatusCode.OK)
        }
        Repository.AccessMode.PRIVATE -> {
            val resultCode = if (isCredentialsValid(request.credentials)) {
                HttpStatusCode.OK
            } else {
                HttpStatusCode.Forbidden
            }
            call.respond(resultCode)
        }
    }
}

private fun RepositoryError.Get.toErrorResponseData(): ResponseData {
    return when (this) {
        RepositoryError.Get.InvalidUserLogin -> ResponseData.fromErrorData(
            code = HttpStatusCode.BadRequest,
            errorMessage = "user does not exist",
        )
        RepositoryError.Get.RepositoryDoesNotExist -> ResponseData.fromErrorData(
            code = HttpStatusCode.BadRequest,
            errorMessage = "repository does not exist",
        )
    }
}

// TODO check credentials by some domain obj
private fun isCredentialsValid(credentials: String?): Boolean {
    return credentials != null
}
