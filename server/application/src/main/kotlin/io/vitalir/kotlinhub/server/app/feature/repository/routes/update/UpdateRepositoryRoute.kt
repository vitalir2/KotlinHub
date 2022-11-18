package io.vitalir.kotlinhub.server.app.feature.repository.routes.update

import arrow.core.Either
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.extensions.respondWith
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.UpdateRepositoryData
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.UpdateRepositoryResult
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.UpdateRepositoryUseCase
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.infrastructure.auth.requireParameter
import io.vitalir.kotlinhub.server.app.infrastructure.auth.userId

internal fun Route.updateRepositoryForCurrentUserRoute(
    updateRepositoryUseCase: UpdateRepositoryUseCase,
) {
    put("/{repositoryName}") {
        val userId = call.userId
        val repositoryName = call.requireParameter("repositoryName")
        val requestBody = call.receive<UpdateRepositoryRequest>()

        val result = updateRepositoryUseCase(
            userIdentifier = UserIdentifier.Id(userId),
            repositoryName = repositoryName,
            updateRepositoryData = UpdateRepositoryData(
                accessMode = requestBody.accessMode,
                updatedAt = UpdateRepositoryData.Time.Now,
            )
        )

        call.respondWith(result.toResponseData())
    }
}

private fun UpdateRepositoryResult.toResponseData(): ResponseData {
    return when (this) {
        is Either.Left -> value.toResponseData()
        is Either.Right -> ResponseData(code = HttpStatusCode.OK)
    }
}

private fun UpdateRepositoryUseCase.Error.toResponseData(): ResponseData {
    return when (this) {
        is UpdateRepositoryUseCase.Error.RepositoryDoesNotExist -> {
            ResponseData.badRequest("Repository $repositoryName for user with identifier=$userIdentifier does not exist")
        }
        is UpdateRepositoryUseCase.Error.UserDoesNotExist -> {
            ResponseData.badRequest("User with identifier=$userIdentifier does not exist")

        }
    }
}
