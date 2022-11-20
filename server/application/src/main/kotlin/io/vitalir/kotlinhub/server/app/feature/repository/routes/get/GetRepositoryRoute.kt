package io.vitalir.kotlinhub.server.app.feature.repository.routes.get

import arrow.core.Either
import io.bkbn.kompendium.core.metadata.GetInfo
import io.bkbn.kompendium.core.plugin.NotarizedRoute
import io.bkbn.kompendium.json.schema.definition.TypeDefinition
import io.bkbn.kompendium.oas.payload.Parameter
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.ErrorResponse
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.extensions.respondWith
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.GetRepositoryUseCase
import io.vitalir.kotlinhub.server.app.feature.repository.routes.common.repositoriesTag
import io.vitalir.kotlinhub.server.app.feature.repository.routes.toApiModel
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.infrastructure.docs.kompendiumDocs
import io.vitalir.kotlinhub.server.app.infrastructure.docs.resType

internal fun Route.userRepositoryRoute(
    getRepositoryUseCase: GetRepositoryUseCase,
) {
    route("/{userId}/{repositoryName}") {
        kompendiumDocs {
            repositoriesTag()
            getRepositoryDocs()
        }
        getRepositoryRoute(getRepositoryUseCase)
    }
}

private fun NotarizedRoute.Config.getRepositoryDocs() {
    get = GetInfo.builder {
        summary("Get repository by userId and repository name")
        description("")
        parameters = listOf(
            Parameter(
                name = "userId",
                `in` = Parameter.Location.path,
                required = true,
                schema = TypeDefinition.INT,
            ),
            Parameter(
                name = "repositoryName",
                `in` = Parameter.Location.path,
                required = true,
                schema = TypeDefinition.STRING,
            ),
        )
        response {
            resType<GetRepositoryResponse>()
            responseCode(HttpStatusCode.OK)
            description("OK")
        }
        canRespond {
            resType<ErrorResponse>()
            responseCode(HttpStatusCode.NotFound)
            description("Not found user or repository, see 'message' for more information")
        }
    }
}

private fun Route.getRepositoryRoute(
    getRepositoryUseCase: GetRepositoryUseCase,
) {
    get {
        val userId = call.parameters["userId"]?.toInt() ?: run {
            call.respondWith(ResponseData.badRequest())
            return@get
        }

        val repositoryName = call.parameters["repositoryName"] ?: run {
            call.respondWith(ResponseData.badRequest())
            return@get
        }

        val result = getRepositoryUseCase(
            userIdentifier = UserIdentifier.Id(userId),
            repositoryName = repositoryName,
        )

        when (result) {
            is Either.Left -> {
                call.respondWith(result.value.toResponseData())
            }

            is Either.Right -> {
                call.respondWith(result.value.toResponseData())
            }
        }
    }
}

private fun GetRepositoryUseCase.Error.toResponseData(): ResponseData {
    return when (this) {
        is GetRepositoryUseCase.Error.UserDoesNotExist -> {
            ResponseData.fromErrorData(
                code = HttpStatusCode.NotFound,
                errorMessage = "no user with username=$userIdentifier was found",
            )
        }

        is GetRepositoryUseCase.Error.RepositoryDoesNotExist -> {
            ResponseData.fromErrorData(
                code = HttpStatusCode.NotFound,
                errorMessage = "no repository with name=$repositoryName was found"
            )
        }
    }
}

private fun Repository.toResponseData(): ResponseData {
    val response = GetRepositoryResponse(
        repository = toApiModel(),
    )
    return ResponseData(
        code = HttpStatusCode.OK,
        body = response,
    )
}
