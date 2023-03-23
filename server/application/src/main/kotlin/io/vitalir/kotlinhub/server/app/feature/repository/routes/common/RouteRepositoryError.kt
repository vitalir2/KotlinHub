package io.vitalir.kotlinhub.server.app.feature.repository.routes.common

import io.ktor.server.application.*
import io.ktor.util.pipeline.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.extensions.respondWith
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.error.RepositoryDoesNotExist
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.error.RepositoryError
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.error.RepositoryFilePathDoesNotExist

internal suspend fun PipelineContext<Unit, ApplicationCall>.handleRepositoryError(error: RepositoryError) {
    val respondData = when (error) {
        is RepositoryDoesNotExist ->
            ResponseData.badRequest(message = "Repository ${error.repositoryIdentifier} does not exist")

        is RepositoryFilePathDoesNotExist ->
            ResponseData.badRequest(message = "Repository file path ${error.absolutePath} does not exist")
    }
    call.respondWith(respondData)
}
