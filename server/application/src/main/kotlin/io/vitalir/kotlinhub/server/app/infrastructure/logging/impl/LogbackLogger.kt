package io.vitalir.kotlinhub.server.app.infrastructure.logging.impl

import io.vitalir.kotlinhub.server.app.infrastructure.logging.Logger
import org.slf4j.LoggerFactory

internal class LogbackLogger : Logger {

    private val delegate = LoggerFactory.getLogger(ch.qos.logback.classic.Logger::class.java)

    override fun log(message: String) {
        delegate.info(message)
    }
}
