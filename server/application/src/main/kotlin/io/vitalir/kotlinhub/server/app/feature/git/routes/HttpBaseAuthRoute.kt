package io.vitalir.kotlinhub.server.app.feature.git.routes

import arrow.core.Either
import io.bkbn.kompendium.core.metadata.PostInfo
import io.bkbn.kompendium.core.plugin.NotarizedRoute
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
import io.vitalir.kotlinhub.server.app.infrastructure.docs.badRequestResponse
import io.vitalir.kotlinhub.server.app.infrastructure.docs.kompendiumDocs
import io.vitalir.kotlinhub.server.app.infrastructure.docs.reqType
import io.vitalir.kotlinhub.server.app.infrastructure.docs.resType
import io.vitalir.kotlinhub.server.app.infrastructure.logging.Logger
import io.vitalir.kotlinhub.shared.feature.git.GitAuthRequest

internal fun Route.httpBaseAuth(
    getRepositoryUseCase: GetRepositoryUseCase,
    updateRepositoryUseCase: UpdateRepositoryUseCase,
    authManager: AuthManager,
    logger: Logger,
) {
    route("http/auth/") {
        kompendiumDocs {
            tags = setOf("git")
            httpBaseAuthDocs()
        }
        post {
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
}

private fun NotarizedRoute.Config.httpBaseAuthDocs() {
    post = PostInfo.builder {
        summary("Handles base auth requests from Git Client")
        description("")
        request {
            reqType<GitAuthRequest>()
            description("Auth request")
        }
        response {
            resType<Int>()
            responseCode(HttpStatusCode.OK)
            description("OK")
        }
        canRespond {
            resType<Int>()
            responseCode(HttpStatusCode.Forbidden)
            description("Forbidden")
        }
        badRequestResponse()
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
