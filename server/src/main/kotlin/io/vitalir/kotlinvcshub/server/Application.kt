package io.vitalir.kotlinvcshub.server

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.vitalir.kotlinvcshub.server.plugins.configureRouting
import io.vitalir.kotlinvcshub.server.plugins.configureSecurity
import io.vitalir.kotlinvcshub.server.plugins.configureSerialization

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureSecurity()
        configureSerialization()
        configureRouting()
    }.start(wait = true)
}
