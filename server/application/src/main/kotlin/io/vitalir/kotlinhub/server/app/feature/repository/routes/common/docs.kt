package io.vitalir.kotlinhub.server.app.feature.repository.routes.common

import io.bkbn.kompendium.core.plugin.NotarizedRoute
import io.bkbn.kompendium.json.schema.definition.TypeDefinition
import io.bkbn.kompendium.oas.payload.Parameter

internal fun NotarizedRoute.Config.repositoriesTag() {
    tags = tags + setOf("repositories")
}

internal object RepositoryDocs {
    val repositoryName = Parameter(
        name = "repositoryName",
        `in` = Parameter.Location.path,
        required = true,
        schema = TypeDefinition.STRING,
    )
}
