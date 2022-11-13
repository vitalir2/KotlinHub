package io.vitalir.kotlinhub.server.app.feature.repository.routes.remove

import arrow.core.Either
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.extensions.respondWith
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.RemoveRepositoryUseCase
import io.vitalir.kotlinhub.server.app.infrastructure.auth.requireParameter
import io.vitalir.kotlinhub.server.app.infrastructure.auth.userId

internal fun Route.removeRepositoryForCurrentUserRoute(
    removeRepositoryUseCase: RemoveRepositoryUseCase,
) {
    delete("/{repositoryName}") {
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
    }
}
