package io.vitalir.kotlinhub.server.app.feature.repository.routes.create

import arrow.core.Either
import io.bkbn.kompendium.core.metadata.PostInfo
import io.bkbn.kompendium.core.plugin.NotarizedRoute
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.ErrorResponse
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.extensions.respondWith
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.CreateRepositoryData
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.CreateRepositoryUseCase
import io.vitalir.kotlinhub.server.app.infrastructure.auth.userId
import io.vitalir.kotlinhub.server.app.infrastructure.docs.badRequestResponse
import io.vitalir.kotlinhub.server.app.infrastructure.docs.reqType
import io.vitalir.kotlinhub.server.app.infrastructure.docs.resType
import io.vitalir.kotlinhub.shared.common.network.Url

internal fun Route.createRepositoryRoute(
    createRepositoryUseCase: CreateRepositoryUseCase,
) {
    post {
        val userId = call.userId
        val request = call.receive<CreateRepositoryRequest>()
        val createRepositoryData = CreateRepositoryData(
            ownerId = userId,
            name = request.name,
            accessMode = request.accessMode,
            description = request.description,
        )

        val result = createRepositoryUseCase(createRepositoryData)
        val responseData = result.toCreateRepositoryResponseData()
        call.respondWith(responseData)
    }
}

internal fun NotarizedRoute.Config.createRepositoryDocs() {
    post = PostInfo.builder {
        summary("Create repository")
        description("")
        request {
            reqType<CreateRepositoryRequest>()
            description("Init data")
        }
        response {
            resType<CreateRepositoryResponse>()
            responseCode(HttpStatusCode.Created)
            description("URL of created repository to clone")
        }
        badRequestResponse()
        canRespond {
            resType<ErrorResponse>()
            responseCode(HttpStatusCode.Unauthorized)
            description("Bad token")
        }
        canRespond {
            resType<ErrorResponse>()
            responseCode(HttpStatusCode.InternalServerError)
            description("Server error")
        }
    }
}

private fun Either<CreateRepositoryUseCase.Error, Url>.toCreateRepositoryResponseData(): ResponseData {
    return when (this) {
        is Either.Left -> value.toResponseData()
        is Either.Right -> ResponseData(
            code = HttpStatusCode.Created,
            body = CreateRepositoryResponse(repositoryUrl = value.toString()),
        )
    }
}

private fun CreateRepositoryUseCase.Error.toResponseData(): ResponseData {
    return when (this) {
        is CreateRepositoryUseCase.Error.UserDoesNotExist -> {
            ResponseData.fromErrorData(
                code = HttpStatusCode.Unauthorized,
                errorMessage = "unauthorized",
            )
        }

        is CreateRepositoryUseCase.Error.RepositoryAlreadyExists -> {
            ResponseData.fromErrorData(
                code = HttpStatusCode.BadRequest,
                errorMessage = "repository $repositoryName already exists",
            )
        }

        is CreateRepositoryUseCase.Error.Unknown -> {
            ResponseData.serverError()
        }
    }
}
