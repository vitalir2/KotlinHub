package io.vitalir.kotlinhub.server.app.feature.repository.routes

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.extensions.respondWith
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.routes.common.extensions.userIdOrNull
import io.vitalir.kotlinhub.server.app.infrastructure.auth.userIdOrNull

internal class RepositoryAccessPluginConfig {
    var baseRepositoryUri: String? = null
    var userHasAccessToRepository: suspend (UserIdentifier?, RepositoryIdentifier) -> Boolean
            = { _, _ -> false }
}

internal val repositoryAccessPlugin = createApplicationPlugin("RepositoryAccess", ::RepositoryAccessPluginConfig) {
    val baseRepositoryUri = pluginConfig.baseRepositoryUri
        ?: throw IllegalStateException("No baseRepositoryUri was provided")
    onCall { call ->
        if (call.request.uri.startsWith(baseRepositoryUri)) {
            // TODO change logic
            val currentUserId = call.userIdOrNull
            val ownerIdentifier = call.parameters.userIdOrNull(currentUserId) ?: return@onCall
            val repositoryName = call.parameters["repositoryName"] ?: return@onCall

            val userIdentifier = currentUserId?.let(UserIdentifier::Id)
            val repositoryIdentifier = RepositoryIdentifier.OwnerIdentifierAndName(
                ownerIdentifier, repositoryName
            )
            if (pluginConfig.userHasAccessToRepository(userIdentifier, repositoryIdentifier).not()) {
                call.respondWith(ResponseData.forbidden())
            }
        }
    }
}
