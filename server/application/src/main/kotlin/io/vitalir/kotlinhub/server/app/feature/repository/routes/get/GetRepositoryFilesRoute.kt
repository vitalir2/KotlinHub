package io.vitalir.kotlinhub.server.app.feature.repository.routes.get

import arrow.core.Either
import io.bkbn.kompendium.core.metadata.GetInfo
import io.bkbn.kompendium.core.plugin.NotarizedRoute
import io.bkbn.kompendium.json.schema.definition.TypeDefinition
import io.bkbn.kompendium.oas.payload.Parameter
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
import io.vitalir.kotlinhub.server.app.feature.repository.routes.common.RepositoryDocs
import io.vitalir.kotlinhub.server.app.feature.repository.routes.common.repositoriesTag
import io.vitalir.kotlinhub.server.app.feature.user.routes.common.extensions.userId
import io.vitalir.kotlinhub.server.app.feature.user.routes.common.userIdParam
import io.vitalir.kotlinhub.server.app.infrastructure.auth.requireParameter
import io.vitalir.kotlinhub.server.app.infrastructure.auth.userIdOrNull
import io.vitalir.kotlinhub.server.app.infrastructure.docs.kompendiumDocs
import io.vitalir.kotlinhub.server.app.infrastructure.docs.resType
import io.vitalir.kotlinhub.shared.common.ErrorResponse
import io.vitalir.kotlinhub.shared.feature.repository.ApiRepositoryFile
import io.vitalir.kotlinhub.shared.feature.repository.GetRepositoryFilesResponse

internal fun Route.getRepositoryFiles(
    getRepositoryDirFilesUseCase: GetRepositoryDirFilesUseCase,
) {
    route("/{userId}/{repositoryName}/tree/{absolutePath...}") {
        kompendiumDocs {
            repositoriesTag()
            getRepositoryFilesDocs()
        }
        get {
            val currentUserId = call.userIdOrNull
            val userIdentifier = call.parameters.userId(currentUserId)
            val repositoryName = call.requireParameter("repositoryName")
            val absolutePath = call.parameters.getAll("absolutePath")
                .orEmpty()
                .joinToString(separator = "/")

            val result = getRepositoryDirFilesUseCase(
                repositoryIdentifier = RepositoryIdentifier.OwnerIdentifierAndName(
                    userIdentifier, repositoryName
                ),
                absolutePath = absolutePath,
            )

            when (result) {
                is Either.Left -> handleRepositoryError(result.value)
                is Either.Right -> call.respondWith(
                    ResponseData(
                        code = HttpStatusCode.OK,
                        body = GetRepositoryFilesResponse(
                            arrayOf(*result.value.map { it.toApiModel() }.toTypedArray())
                        )
                    )
                )
            }
        }
    }
}

private fun NotarizedRoute.Config.getRepositoryFilesDocs() {
    get = GetInfo.builder {
        summary("Get repository files")
        description("")
        parameters = listOf(
            userIdParam,
            RepositoryDocs.repositoryName,
            Parameter(
                name = "absolutePath",
                `in` = Parameter.Location.path,
                required = true,
                schema = TypeDefinition.STRING,
                description = "Absolute path to a folder of the repository",
            )
        )
        response {
            resType<GetRepositoryFilesResponse>()
            responseCode(HttpStatusCode.OK)
            description("OK")
        }
        response {
            resType<ErrorResponse>()
            responseCode(HttpStatusCode.BadRequest)
            description("Invalid request params, see `message` property for more details")
        }
    }
}

internal fun RepositoryFile.toApiModel(): ApiRepositoryFile {
    return ApiRepositoryFile(
        name = name,
        type = when (type) {
            RepositoryFile.Type.FOLDER -> ApiRepositoryFile.Type.FOLDER
            RepositoryFile.Type.REGULAR -> ApiRepositoryFile.Type.REGULAR
            RepositoryFile.Type.UNKNOWN -> ApiRepositoryFile.Type.UNKNOWN
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
