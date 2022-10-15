package io.vitalir.kotlinvcshub.server

import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.netty.*
import io.vitalir.kotlinvcshub.server.infrastructure.config.AppConfig
import io.vitalir.kotlinvcshub.server.plugins.configureRouting
import io.vitalir.kotlinvcshub.server.plugins.configureSerialization

fun main(args: Array<String>) = EngineMain.main(args)

// Used in application config
@Suppress("UNUSED")
fun Application.mainModule() {
    val parsedConfig = environment.config.toAppConfig()
    // TODO configureSecurity()
    configureSerialization()
    configureRouting()
}

private fun ApplicationConfig.toAppConfig(): AppConfig =
    AppConfig(
        jwtConfig = AppConfig.Jwt(
            secret = property("jwt.secret").getString(),
            issuer = property("jwt.issuer").getString(),
            audience = property("jwt.audience").getString(),
            realm = property("jwt.realm").getString(),
        )
    )
