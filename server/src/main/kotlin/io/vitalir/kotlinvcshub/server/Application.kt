package io.vitalir.kotlinvcshub.server

import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.vitalir.kotlinvcshub.server.plugins.configureRouting
import io.vitalir.kotlinvcshub.server.plugins.configureSerialization

fun main(args: Array<String>) = EngineMain.main(args)

// Used in application config
@Suppress("UNUSED")
fun Application.mainModule() {
    // TODO configureSecurity()
    configureSerialization()
    configureRouting()
}
