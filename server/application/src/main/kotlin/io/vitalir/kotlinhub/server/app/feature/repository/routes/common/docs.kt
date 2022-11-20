package io.vitalir.kotlinhub.server.app.feature.repository.routes.common

import io.bkbn.kompendium.core.plugin.NotarizedRoute

internal fun NotarizedRoute.Config.repositoriesTag() {
    tags = tags + setOf("repositories")
}
