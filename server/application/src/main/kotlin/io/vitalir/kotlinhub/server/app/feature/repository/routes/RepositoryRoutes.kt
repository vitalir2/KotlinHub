package io.vitalir.kotlinhub.server.app.feature.repository.routes

import arrow.core.Either
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.extensions.respondWith
import io.vitalir.kotlinhub.server.app.common.routes.jwtAuth
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.CreateRepositoryData
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryError
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.CreateRepositoryUseCase
import io.vitalir.kotlinhub.server.app.feature.repository.routes.getrepository.getRepositoryRoute
import io.vitalir.kotlinhub.server.app.infrastructure.auth.userId
import io.vitalir.kotlinhub.server.app.infrastructure.di.AppGraph
import io.vitalir.kotlinhub.shared.common.network.Url

internal fun Routing.repositoryRoutes(
    repositoryGraph: AppGraph.RepositoryGraph,
) {
    route("repositories/") {
        createRepositoryRoute(repositoryGraph.createRepositoryUseCase)
        getRepositoryRoute(repositoryGraph.getRepositoryUseCase)
    }
}

private fun Route.createRepositoryRoute(
    createRepositoryUseCase: CreateRepositoryUseCase,
) {
    jwtAuth {
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
}

private fun Either<RepositoryError.Create, Url>.toCreateRepositoryResponseData(): ResponseData {
    return when (this) {
        is Either.Left -> value.toResponseData()
        is Either.Right -> ResponseData(
            code = HttpStatusCode.Created,
            body = CreateRepositoryResponse(repositoryUrl = value.toString()),
        )
    }
}

private fun RepositoryError.Create.toResponseData(): ResponseData {
    return when (this) {
        is RepositoryError.Create.InvalidUserId -> ResponseData.fromErrorData(
            code = HttpStatusCode.Unauthorized,
            errorMessage = "unauthorized",
        )
        is RepositoryError.Create.RepositoryAlreadyExists -> ResponseData.fromErrorData(
            code = HttpStatusCode.BadRequest,
            errorMessage = "repository $repositoryName already exists",
        )
    }
}
