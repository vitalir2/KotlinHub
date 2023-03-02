package io.vitalir.kotlinhub.server.app.feature.repository.routes.get

import arrow.core.Either
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.extensions.respondWith
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryFile
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryIdentifier
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.error.RepositoryDoesNotExist
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.error.RepositoryError
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.error.RepositoryFilePathDoesNotExist
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.GetRepositoryDirFilesUseCase
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.infrastructure.auth.requireParameter
import io.vitalir.kotlinhub.shared.feature.repository.ApiRepositoryFile
import io.vitalir.kotlinhub.shared.feature.repository.GetRepositoryFilesResponse

internal fun Route.getRepositoryFiles(
    getRepositoryDirFilesUseCase: GetRepositoryDirFilesUseCase,
) {
    route("/{userId}/{repositoryName}/tree/{absolutePath...}") {
        get {
            val userId = call.requireParameter("userId", String::toInt)
            val repositoryName = call.requireParameter("repositoryName")
            val absolutePath = call.parameters.getAll("absolutePath")
                .orEmpty()
                .joinToString(separator = "/")

            val result = getRepositoryDirFilesUseCase(
                repositoryIdentifier = RepositoryIdentifier.OwnerIdentifierAndName(
                    UserIdentifier.Id(userId), repositoryName
                ),
                absolutePath = absolutePath,
            )

            when (result) {
                is Either.Left -> handleRepositoryError(result.value)
                is Either.Right -> ResponseData(
                    code = HttpStatusCode.OK,
                    body = GetRepositoryFilesResponse(
                        arrayOf(*result.value.map { it.toApiModel() }.toTypedArray())
                    )
                )
            }
        }
    }
}

internal fun RepositoryFile.toApiModel(): ApiRepositoryFile {
    return ApiRepositoryFile(
        name = name,
        type = when (type) {
            RepositoryFile.Type.FOLDER -> ApiRepositoryFile.Type.FOLDER
            RepositoryFile.Type.SIMPLE -> ApiRepositoryFile.Type.SIMPLE
        },
    )
}

internal suspend fun PipelineContext<Unit, ApplicationCall>.handleRepositoryError(error: RepositoryError) {
    val respondData = when (error) {
        is RepositoryDoesNotExist ->
            ResponseData.badRequest(message = "Repository ${error.repositoryIdentifier} does not exist")
        is RepositoryFilePathDoesNotExist ->
            ResponseData.badRequest(message = "Repository file path ${error.absolutePath} does not exist")
    }
    call.respondWith(respondData)
}
