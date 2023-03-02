package io.vitalir.kotlinhub.server.app.feature.repository.routes

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.extensions.respondWith
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.infrastructure.auth.requireParameter
import io.vitalir.kotlinhub.server.app.infrastructure.auth.userIdOrNull

internal class RepositoryAccessPluginConfig {
    var baseRepositoryUri: String? = null
    var userHasAccessToRepository: (UserIdentifier?, RepositoryIdentifier) -> Boolean = { _, _ -> false }
}

internal val repositoryAccessPlugin = createApplicationPlugin("RepositoryAccess", ::RepositoryAccessPluginConfig) {
    val baseRepositoryUri = pluginConfig.baseRepositoryUri
        ?: throw IllegalStateException("No baseRepositoryUri was provided")
    onCall { call ->
        if (call.request.uri.startsWith(baseRepositoryUri)) {
            // TODO change logic
            val ownerId = call.requireParameter("userId", String::toInt)
            val repositoryName = call.requireParameter("repositoryName")
            val currentUserId = call.userIdOrNull

            val userIdentifier = currentUserId?.let(UserIdentifier::Id)
            val repositoryIdentifier = RepositoryIdentifier.OwnerIdentifierAndName(
                UserIdentifier.Id(ownerId), repositoryName
            )
            if (pluginConfig.userHasAccessToRepository(userIdentifier, repositoryIdentifier).not()) {
                call.respondWith(ResponseData.forbidden())
            }
        }
    }
}
