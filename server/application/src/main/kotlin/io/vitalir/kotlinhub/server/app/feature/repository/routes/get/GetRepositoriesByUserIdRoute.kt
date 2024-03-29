package io.vitalir.kotlinhub.server.app.feature.repository.routes.get

import arrow.core.Either
import io.bkbn.kompendium.core.metadata.GetInfo
import io.bkbn.kompendium.core.plugin.NotarizedRoute
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.extensions.respondWith
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.GetRepositoriesForUserUseCase
import io.vitalir.kotlinhub.server.app.feature.repository.routes.common.repositoriesTag
import io.vitalir.kotlinhub.server.app.feature.repository.routes.toApiModel
import io.vitalir.kotlinhub.server.app.feature.repository.routes.toResponseData
import io.vitalir.kotlinhub.server.app.feature.user.routes.common.extensions.userId
import io.vitalir.kotlinhub.server.app.feature.user.routes.common.userIdParam
import io.vitalir.kotlinhub.server.app.infrastructure.auth.userIdOrNull
import io.vitalir.kotlinhub.server.app.infrastructure.docs.badRequestResponse
import io.vitalir.kotlinhub.server.app.infrastructure.docs.kompendiumDocs
import io.vitalir.kotlinhub.server.app.infrastructure.docs.resType
import io.vitalir.kotlinhub.shared.feature.repository.GetRepositoriesResponse

internal fun Route.userRepositoriesRoute(
    getRepositoriesForUserUseCase: GetRepositoriesForUserUseCase,
) {
    route("/{userId}") {
        kompendiumDocs {
            repositoriesTag()
            getUserRepositoriesDocs()
        }
        getUserRepositoriesRoute(getRepositoriesForUserUseCase)
    }
}

private fun NotarizedRoute.Config.getUserRepositoriesDocs() {
    get = GetInfo.builder {
        summary("Get repositories for user")
        description("")
        parameters = listOf(
            userIdParam,
        )
        response {
            resType<GetRepositoriesResponse>()
            responseCode(HttpStatusCode.OK)
            description("OK")
        }
        badRequestResponse()
    }
}

private fun Route.getUserRepositoriesRoute(
    getRepositoriesForUserUseCase: GetRepositoriesForUserUseCase,
) {
    get {
        val currentUserId = call.userIdOrNull
        val userId = call.parameters.userId(currentUserId)
        val responseData = when (val result = getRepositoriesForUserUseCase(currentUserId, userId)) {
            is Either.Left -> result.value.toResponseData()
            is Either.Right -> ResponseData(
                code = HttpStatusCode.OK,
                body = GetRepositoriesResponse(
                    repositories = result.value.map(Repository::toApiModel).toTypedArray(),
                ),
            )
        }
        call.respondWith(responseData)
    }
}

private fun GetRepositoriesForUserUseCase.Error.toResponseData(): ResponseData {
    return when (this) {
        is GetRepositoriesForUserUseCase.Error.UserDoesNotExist -> toResponseData()
    }
}
