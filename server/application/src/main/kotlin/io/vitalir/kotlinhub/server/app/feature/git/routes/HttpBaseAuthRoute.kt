package io.vitalir.kotlinhub.server.app.feature.git.routes

import io.bkbn.kompendium.core.metadata.PostInfo
import io.bkbn.kompendium.core.plugin.NotarizedRoute
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.extensions.respondWith
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryIdentifier
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.HasUserAccessToRepositoryUseCase
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserCredentials
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.infrastructure.docs.badRequestResponse
import io.vitalir.kotlinhub.server.app.infrastructure.docs.kompendiumDocs
import io.vitalir.kotlinhub.server.app.infrastructure.docs.reqType
import io.vitalir.kotlinhub.server.app.infrastructure.docs.resType
import io.vitalir.kotlinhub.server.app.infrastructure.routing.BaseAuthValue
import io.vitalir.kotlinhub.server.app.infrastructure.routing.HeaderManager
import io.vitalir.kotlinhub.shared.common.ErrorResponse
import io.vitalir.kotlinhub.shared.feature.git.GitAuthRequest

internal fun Route.httpBaseAuth(
    hasUserAccessToRepositoryUseCase: HasUserAccessToRepositoryUseCase,
    headerManager: HeaderManager<BaseAuthValue>,
) {
    route("http/auth/") {
        kompendiumDocs {
            tags = setOf("git")
            httpBaseAuthDocs()
        }
        post {
            val request = call.receive<GitAuthRequest>()
            val password = request.credentials?.let(headerManager::parse)?.password ?: run {
                call.respondWith(ResponseData.forbidden())
                return@post
            }
            val userIdentifier = UserIdentifier.Id(request.userId)

            val hasAccess = hasUserAccessToRepositoryUseCase(
                userCredentials = UserCredentials(
                    identifier = userIdentifier,
                    password = password
                ),
                repositoryIdentifier = RepositoryIdentifier.OwnerIdentifierAndName(
                    ownerIdentifier = userIdentifier,
                    repositoryName = request.repositoryName,
                ),
            )

            val responseData = if (hasAccess) {
                ResponseData.ok()
            } else {
                ResponseData.forbidden()
            }
            call.respondWith(responseData)
        }
    }
}

private fun NotarizedRoute.Config.httpBaseAuthDocs() {
    post = PostInfo.builder {
        summary("Handles base auth requests from Git Client")
        description("")
        request {
            reqType<GitAuthRequest>()
            description("Auth request")
        }
        response {
            resType<Int>()
            responseCode(HttpStatusCode.OK)
            description("OK")
        }
        canRespond {
            resType<ErrorResponse>()
            responseCode(HttpStatusCode.Forbidden)
            description("Forbidden")
        }
        badRequestResponse()
    }
}
