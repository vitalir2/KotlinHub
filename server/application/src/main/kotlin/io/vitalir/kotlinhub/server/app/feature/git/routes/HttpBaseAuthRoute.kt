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
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.GetRepositoryResult
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.GetRepositoryUseCase
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.UpdateRepositoryData
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.UpdateRepositoryUseCase
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.infrastructure.auth.AuthManager
import io.vitalir.kotlinhub.server.app.infrastructure.logging.Logger
import io.vitalir.kotlinhub.shared.feature.git.GitAuthRequest

internal fun Route.httpBaseAuth(
    getRepositoryUseCase: GetRepositoryUseCase,
    updateRepositoryUseCase: UpdateRepositoryUseCase,
    authManager: AuthManager,
    logger: Logger,
) {
    post("http/auth/") {
        val request = call.receive<GitAuthRequest>()
        val result = getRepositoryUseCase(
            userIdentifier = UserIdentifier.Id(request.userId),
            repositoryName = request.repositoryName,
        )
        handleGetRepositoryResult(
            request = request,
            result = result,
            updateRepositoryUseCase = updateRepositoryUseCase,
            authManager = authManager,
            logger = logger,
        )
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handleGetRepositoryResult(
    request: GitAuthRequest,
    result: GetRepositoryResult,
    updateRepositoryUseCase: UpdateRepositoryUseCase,
    authManager: AuthManager,
    logger: Logger,
) {
    when (result) {
        is Either.Left -> {
            call.respondWith(result.value.toErrorResponseData())
        }
        is Either.Right -> {
            handleRepositoryFromResult(
                request = request,
                repository = result.value,
                updateRepositoryUseCase = updateRepositoryUseCase,
                authManager = authManager,
                logger = logger,
            )
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handleRepositoryFromResult(
    request: GitAuthRequest,
    repository: Repository,
    updateRepositoryUseCase: UpdateRepositoryUseCase,
    authManager: AuthManager,
    logger: Logger,
) {
    when (repository.accessMode) {
        // Permit any call for now
        Repository.AccessMode.PUBLIC -> {
            call.respond(HttpStatusCode.OK)
        }
        Repository.AccessMode.PRIVATE -> {
            val credentials = request.credentials
            logger.log("Credentials: $credentials")
            val isCredentialsValid = credentials != null && authManager.checkCredentials(
                user = repository.owner,
                credentials = credentials,
            )
            val resultCode = if (isCredentialsValid) {
                // TODO may broke easily. To fix it - we need to use our custom reverse proxy server
                updateRepositoryUseCase(
                    userIdentifier = repository.owner.identifier,
                    repositoryName = repository.name,
                    updateRepositoryData = UpdateRepositoryData(updatedAt = UpdateRepositoryData.Time.Now),
                )
                HttpStatusCode.OK
            } else {
                HttpStatusCode.Forbidden
            }
            call.respond(resultCode)
        }
    }
}

private fun GetRepositoryUseCase.Error.toErrorResponseData(): ResponseData {
    return when (this) {
        is GetRepositoryUseCase.Error.UserDoesNotExist -> ResponseData.fromErrorData(
            code = HttpStatusCode.BadRequest,
            errorMessage = "user $userIdentifier does not exist",
        )
        is GetRepositoryUseCase.Error.RepositoryDoesNotExist -> ResponseData.fromErrorData(
            code = HttpStatusCode.BadRequest,
            errorMessage = "repository $repositoryName for user $userIdentifier does not exist",
        )
    }
}
