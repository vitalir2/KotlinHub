package io.vitalir.kotlinhub.server.app.feature.user.routes.common

import io.bkbn.kompendium.core.metadata.MethodInfo
import io.bkbn.kompendium.core.plugin.NotarizedRoute
import io.bkbn.kompendium.json.schema.definition.TypeDefinition
import io.bkbn.kompendium.oas.payload.Parameter

internal fun NotarizedRoute.Config.usersTag() {
    tags = tags + setOf("users")
}

// Extension "scopes" the method
@Suppress("UnusedReceiverParameter")
internal val MethodInfo.Builder<*>.userIdParam: Parameter
    get() = Parameter(
        name = "userId",
        `in` = Parameter.Location.path,
        required = true,
        schema = TypeDefinition.STRING,
        description = """
                    Use "current" if you want to get current user repositories
                    Otherwise, pass userId from the User object
                """.trimIndent(),
    )
