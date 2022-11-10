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
import io.vitalir.kotlinhub.server.app.infrastructure.auth.BasicAuthManager
import io.vitalir.kotlinhub.server.app.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.repository.domain.model.RepositoryError
import io.vitalir.kotlinhub.server.app.repository.domain.usecase.GetRepositoryResult
import io.vitalir.kotlinhub.server.app.repository.domain.usecase.GetRepositoryUseCase
import io.vitalir.kotlinhub.shared.feature.git.GitAuthRequest

internal fun Route.httpBaseAuth(
    getRepositoryUseCase: GetRepositoryUseCase,
    basicAuthManager: BasicAuthManager,
) {
    post("http/auth/") {
        val request = call.receive<GitAuthRequest>()
        val result = getRepositoryUseCase(
            username = request.username,
            repositoryName = request.repositoryName,
        )
        handleGetRepositoryResult(
            request = request,
            result = result,
            basicAuthManager = basicAuthManager,
        )
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handleGetRepositoryResult(
    request: GitAuthRequest,
    result: GetRepositoryResult,
    basicAuthManager: BasicAuthManager,
) {
    when (result) {
        is Either.Left -> {
            call.respondWith(result.value.toErrorResponseData())
        }
        is Either.Right -> {
            handleRepositoryFromResult(
                request = request,
                repository = result.value,
                basicAuthManager = basicAuthManager,
            )
        }
    }
}

// TODO pass logger here
private suspend fun PipelineContext<Unit, ApplicationCall>.handleRepositoryFromResult(
    request: GitAuthRequest,
    repository: Repository,
    basicAuthManager: BasicAuthManager,
) {
    when (repository.accessMode) {
        // Permit any call for now
        Repository.AccessMode.PUBLIC -> {
            call.respond(HttpStatusCode.OK)
        }
        Repository.AccessMode.PRIVATE -> {
            val credentials = request.credentials
            application.log.debug("Credentials APP: $credentials")
            val isCredentialsValid = credentials != null && basicAuthManager.checkCredentials(
                user = repository.owner,
                credentialsInBase64 = credentials,
            )
            val resultCode = if (isCredentialsValid) {
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
