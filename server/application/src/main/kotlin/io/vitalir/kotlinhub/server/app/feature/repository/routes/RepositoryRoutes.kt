package io.vitalir.kotlinhub.server.app.feature.repository.routes

import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.jwtAuth
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryError
import io.vitalir.kotlinhub.server.app.feature.repository.routes.common.repositoriesTag
import io.vitalir.kotlinhub.server.app.feature.repository.routes.create.createRepositoryDocs
import io.vitalir.kotlinhub.server.app.feature.repository.routes.create.createRepositoryRoute
import io.vitalir.kotlinhub.server.app.feature.repository.routes.get.getRepositoryFileContent
import io.vitalir.kotlinhub.server.app.feature.repository.routes.get.getRepositoryFiles
import io.vitalir.kotlinhub.server.app.feature.repository.routes.get.userRepositoriesRoute
import io.vitalir.kotlinhub.server.app.feature.repository.routes.get.userRepositoryRoute
import io.vitalir.kotlinhub.server.app.feature.repository.routes.remove.repositoryIdRoute
import io.vitalir.kotlinhub.server.app.infrastructure.di.AppGraph
import io.vitalir.kotlinhub.server.app.infrastructure.docs.kompendiumDocs

internal fun Route.repositoryRoutes(
    repositoryGraph: AppGraph.RepositoryGraph,
) {
    route("repositories/") {
        jwtAuth(optional = true) {
            userRepositoryRoute(repositoryGraph.getRepositoryUseCase)
            userRepositoriesRoute(repositoryGraph.getRepositoriesForUserUseCase)
            getRepositoryFiles(repositoryGraph.getRepositoryDirFilesUseCase)
            getRepositoryFileContent(repositoryGraph.repositoryTreePersistence)
        }

        jwtAuth {
            kompendiumDocs {
                repositoriesTag()
                createRepositoryDocs()
            }
            createRepositoryRoute(repositoryGraph.createRepositoryUseCase)
            repositoryIdRoute(repositoryGraph)
        }
    }
}

internal fun RepositoryError.toResponseData(): ResponseData {
    return when (this) {
        is RepositoryError.RepositoryDoesNotExist -> ResponseData.badRequest(
            "repository $repositoryName for user $userIdentifier does not exist",
        )
        is RepositoryError.Unknown -> ResponseData.serverError()
        is RepositoryError.UserDoesNotExist -> ResponseData.badRequest(
            message = "user $userIdentifier does not exist",
        )
    }
}
