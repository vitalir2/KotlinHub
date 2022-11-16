package io.vitalir.kotlinhub.server.app.feature.repository.routes.get

import arrow.core.Either
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.extensions.respondWith
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.GetRepositoryUseCase
import io.vitalir.kotlinhub.server.app.feature.repository.routes.ApiRepository
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier

internal fun Route.getRepositoryRoute(
    getRepositoryUseCase: GetRepositoryUseCase,
) {
    get("/{userId}/{repositoryName}") {
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
        repository = ApiRepository(
            ownerId = owner.id,
            name = name,
            accessMode = accessMode,
            createdAt = createdAt,
            updatedAt = updatedAt,
            description = description,
        )
    )
    return ResponseData(
        code = HttpStatusCode.OK,
        body = response,
    )
}
