package io.vitalir.kotlinhub.server.app.feature.repository.routes.getrepository

import arrow.core.Either
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.extensions.respondWith
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryError
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.GetRepositoryUseCase
import io.vitalir.kotlinhub.server.app.feature.repository.routes.ApiRepository

internal fun Route.getRepositoryRoute(
    getRepositoryUseCase: GetRepositoryUseCase,
) {
    get("/{username}/{repositoryName}") {
        val username = call.parameters["username"] ?: run {
            call.respondWith(ResponseData.badRequest())
            return@get
        }

        val repositoryName = call.parameters["repositoryName"] ?: run {
            call.respondWith(ResponseData.badRequest())
            return@get
        }

        val result = getRepositoryUseCase(
            username = username,
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

private fun RepositoryError.Get.toResponseData(): ResponseData {
    return when (this) {
        is RepositoryError.Get.UserDoesNotExist -> {
            ResponseData.fromErrorData(
                code = HttpStatusCode.NotFound,
                errorMessage = "no user with username=$userIdentifier was found",
            )
        }
        is RepositoryError.Get.RepositoryDoesNotExist -> {
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
