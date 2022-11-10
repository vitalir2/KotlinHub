package io.vitalir.kotlinhub.server.app.infrastructure.logging.impl

import io.ktor.util.logging.*
import io.vitalir.kotlinhub.server.app.infrastructure.logging.Logger as KotlinHubLogger

internal class KtorLogger(
    private val delegate: Logger,
) : KotlinHubLogger {

    override fun log(message: String) {
        delegate.info(message)
    }
}
