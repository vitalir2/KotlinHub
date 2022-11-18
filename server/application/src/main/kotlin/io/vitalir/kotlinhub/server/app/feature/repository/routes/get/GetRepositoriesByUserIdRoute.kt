package io.vitalir.kotlinhub.server.app.feature.repository.routes.get

import arrow.core.Either
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.extensions.respondWith
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.GetRepositoriesForUserUseCase
import io.vitalir.kotlinhub.server.app.feature.repository.routes.toApiModel
import io.vitalir.kotlinhub.server.app.feature.repository.routes.toResponseData
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.infrastructure.auth.requireParameter

internal fun Route.getRepositoriesByUserId(
    getRepositoriesForUserUseCase: GetRepositoriesForUserUseCase,
) {
    get("/{userId}") {
        val userIdentifier = call.requireParameter("userId", String::toInt)
            .let(UserIdentifier::Id)
        val responseData = when (val result = getRepositoriesForUserUseCase(userIdentifier)) {
            is Either.Left -> result.value.toResponseData()
            is Either.Right -> ResponseData(
                code = HttpStatusCode.OK,
                body = GetRepositoriesResponse(
                    repositories = result.value.map(Repository::toApiModel),
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
