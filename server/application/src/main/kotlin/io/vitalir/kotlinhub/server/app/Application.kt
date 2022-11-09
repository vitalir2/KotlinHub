package io.vitalir.kotlinhub.server.app

import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.netty.*
import io.vitalir.kotlinhub.server.app.infrastructure.config.AppConfig
import io.vitalir.kotlinhub.server.app.infrastructure.di.AppGraphFactory
import io.vitalir.kotlinhub.server.app.infrastructure.di.AppGraphFactoryImpl
import io.vitalir.kotlinhub.server.app.plugins.configureRouting
import io.vitalir.kotlinhub.server.app.plugins.configureSecurity
import io.vitalir.kotlinhub.server.app.plugins.configureSerialization

fun main(args: Array<String>) = EngineMain.main(args)

// Used in application config
@Suppress("UNUSED")
fun Application.mainModule() {
    val appConfig = environment.config.toAppConfig()
    val appGraphFactory: AppGraphFactory = AppGraphFactoryImpl(this)
    val applicationGraph = appGraphFactory.create(appConfig)

    configureSecurity(jwtConfig = appConfig.jwt)
    configureSerialization()
    configureRouting(applicationGraph)
}

private fun ApplicationConfig.toAppConfig(): AppConfig {
    val isDevelopmentMode = property("ktor.development").getString().toBoolean()
    return AppConfig(
        debug = if (isDevelopmentMode) config("debug").debugConfig else null,
        jwt = config("jwt").jwtConfig,
        database = config("database").databaseConfig,
    )
}

private val ApplicationConfig.jwtConfig: AppConfig.Jwt
    get() = AppConfig.Jwt(
        secret = property("secret").getString(),
        issuer = property("issuer").getString(),
        audience = property("audience").getString(),
        realm = property("realm").getString(),
    )

private val ApplicationConfig.databaseConfig: AppConfig.Database
    get() = AppConfig.Database(
        username = property("username").getString(),
        password = property("password").getString(),
        databaseName = property("databaseName").getString(),
        serverName = property("serverName").getString(),
    )

private val ApplicationConfig.debugConfig: AppConfig.Debug
    get() = AppConfig.Debug(
        isRoutesTracingEnabled = property("routeTracing").getString().toBoolean(),
    )
