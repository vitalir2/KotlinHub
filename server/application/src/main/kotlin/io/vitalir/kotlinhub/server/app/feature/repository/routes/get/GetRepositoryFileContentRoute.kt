package io.vitalir.kotlinhub.server.app.feature.repository.routes.get


import arrow.core.Either
import io.bkbn.kompendium.core.metadata.GetInfo
import io.bkbn.kompendium.core.plugin.NotarizedRoute
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryIdentifier
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryTreePersistence
import io.vitalir.kotlinhub.server.app.feature.repository.routes.common.RepositoryDocs
import io.vitalir.kotlinhub.server.app.feature.repository.routes.common.handleRepositoryError
import io.vitalir.kotlinhub.server.app.feature.repository.routes.common.repositoriesTag
import io.vitalir.kotlinhub.server.app.feature.user.routes.common.extensions.userId
import io.vitalir.kotlinhub.server.app.feature.user.routes.common.userIdParam
import io.vitalir.kotlinhub.server.app.infrastructure.auth.requireParameter
import io.vitalir.kotlinhub.server.app.infrastructure.auth.userIdOrNull
import io.vitalir.kotlinhub.server.app.infrastructure.docs.kompendiumDocs
import io.vitalir.kotlinhub.server.app.infrastructure.docs.resType
import io.vitalir.kotlinhub.shared.common.ErrorResponse
import io.vitalir.kotlinhub.shared.feature.repository.GetRepositoryFileContentResponse

internal fun Route.getRepositoryFileContent(
    repositoryTreePersistence: RepositoryTreePersistence,
) {
    kompendiumDocs {
        repositoriesTag()
        getRepositoryFileContentDocs()
    }
    get("/{userId}/{repositoryName}/content/{absolutePath...}") {
        val currentUserId = call.userIdOrNull
        val userIdentifier = call.parameters.userId(currentUserId)
        val repositoryName = call.requireParameter("repositoryName")
        val absolutePath = call.parameters.getAll("absolutePath")
            .orEmpty()
            .joinToString(separator = "/")

        val result = repositoryTreePersistence.getFileContent(
            repositoryIdentifier = RepositoryIdentifier.OwnerIdentifierAndName(
                ownerIdentifier = userIdentifier,
                repositoryName = repositoryName,
            ),
            absolutePath = absolutePath,
        )

        when (result) {
            is Either.Left -> handleRepositoryError(result.value)
            is Either.Right -> {
                call.respondBytes(
                    bytes = result.value,
                    status = HttpStatusCode.OK,
                )
            }
        }
    }
}

private fun NotarizedRoute.Config.getRepositoryFileContentDocs() {
    get = GetInfo.builder {
        summary("Get repository file content for the specified repository by username and repositoryName")
        description("")
        parameters = listOf(
            userIdParam,
            RepositoryDocs.repositoryName,
            RepositoryDocs.absolutePath.copy(
                description = "Absolute path to a file of the repository",
            ),
        )
        response {
            resType<GetRepositoryFileContentResponse>()
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
