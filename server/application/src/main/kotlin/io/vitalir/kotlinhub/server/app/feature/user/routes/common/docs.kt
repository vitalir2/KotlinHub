package io.vitalir.kotlinhub.server.app.feature.user.routes.common

import io.bkbn.kompendium.core.plugin.NotarizedRoute

internal fun NotarizedRoute.Config.usersTag() {
    tags = tags + setOf("users")
}
