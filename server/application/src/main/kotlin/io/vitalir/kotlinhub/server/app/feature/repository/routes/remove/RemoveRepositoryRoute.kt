package io.vitalir.kotlinhub.server.app.feature.repository.routes.remove

import arrow.core.Either
import io.bkbn.kompendium.core.metadata.DeleteInfo
import io.bkbn.kompendium.core.plugin.NotarizedRoute
import io.bkbn.kompendium.json.schema.definition.TypeDefinition
import io.bkbn.kompendium.oas.payload.Parameter
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.extensions.respondWith
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.RemoveRepositoryUseCase
import io.vitalir.kotlinhub.server.app.feature.repository.routes.common.repositoriesTag
import io.vitalir.kotlinhub.server.app.feature.repository.routes.update.updateRepositoryForCurrentUserDocs
import io.vitalir.kotlinhub.server.app.feature.repository.routes.update.updateRepositoryForCurrentUserRoute
import io.vitalir.kotlinhub.server.app.infrastructure.auth.requireParameter
import io.vitalir.kotlinhub.server.app.infrastructure.auth.userId
import io.vitalir.kotlinhub.server.app.infrastructure.di.AppGraph
import io.vitalir.kotlinhub.server.app.infrastructure.docs.badRequestResponse
import io.vitalir.kotlinhub.server.app.infrastructure.docs.kompendiumDocs
import io.vitalir.kotlinhub.server.app.infrastructure.docs.resType
import io.vitalir.kotlinhub.server.app.infrastructure.docs.serverErrorResponse

internal fun Route.repositoryIdRoute(
    repositoryGraph: AppGraph.RepositoryGraph,
) {
    route("/{repositoryName}") {
        kompendiumDocs {
            repositoriesTag()
            removeRepositoryDocs()
            updateRepositoryForCurrentUserDocs()
        }
        removeRepositoryForCurrentUserRoute(repositoryGraph.removeRepositoryUseCase)
        updateRepositoryForCurrentUserRoute(repositoryGraph.updateRepositoryUseCase)
    }
}

private fun NotarizedRoute.Config.removeRepositoryDocs() {
    delete = DeleteInfo.builder {
        summary("Remove repository for current user by id")
        description("")
        parameters = listOf(
            Parameter(
                name = "repositoryName",
                `in` = Parameter.Location.path,
                required = true,
                schema = TypeDefinition.INT,
            ),
        )
        response {
            resType<Int>()
            responseCode(HttpStatusCode.OK)
            description("OK")
        }
        badRequestResponse()
        serverErrorResponse()
    }
}

internal fun Route.removeRepositoryForCurrentUserRoute(
    removeRepositoryUseCase: RemoveRepositoryUseCase,
) {
    delete {
        val userId = call.userId
        val repositoryName = call.requireParameter("repositoryName")
        val result = removeRepositoryUseCase(
            userId = userId,
            repositoryName = repositoryName,
        )
        when (result) {
            is Either.Left -> {
                call.respondWith(result.value.toResponseData())
            }
            is Either.Right -> {
                call.respondWith(
                    ResponseData(code = HttpStatusCode.OK)
                )
            }
        }
    }
}


private fun RemoveRepositoryUseCase.Error.toResponseData(): ResponseData {
    return when (this) {
        is RemoveRepositoryUseCase.Error.RepositoryDoesNotExist -> {
            ResponseData.fromErrorData(
                code = HttpStatusCode.BadRequest,
                errorMessage = "repository $repositoryName for user with identifier=$userIdentifier does not exist",
            )
        }
        is RemoveRepositoryUseCase.Error.UserDoesNotExist -> {
            ResponseData.fromErrorData(
                code = HttpStatusCode.BadRequest,
                errorMessage = "user with identifier=$userIdentifier does not exist",
            )
        }
        is RemoveRepositoryUseCase.Error.Unknown -> {
            ResponseData.serverError()
        }
    }
}
