package io.vitalir.kotlinvcshub.server

import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.netty.*
import io.vitalir.kotlinvcshub.server.infrastructure.config.AppConfig
import io.vitalir.kotlinvcshub.server.infrastructure.di.AppGraphFactory
import io.vitalir.kotlinvcshub.server.infrastructure.di.AppGraphFactoryImpl
import io.vitalir.kotlinvcshub.server.plugins.configureRouting
import io.vitalir.kotlinvcshub.server.plugins.configureSecurity
import io.vitalir.kotlinvcshub.server.plugins.configureSerialization

fun main(args: Array<String>) = EngineMain.main(args)

// Used in application config
@Suppress("UNUSED")
fun Application.mainModule() {
    val appConfig = environment.config.toAppConfig()
    val appGraphFactory: AppGraphFactory = AppGraphFactoryImpl()
    val applicationGraph = appGraphFactory.create(appConfig)

    configureSecurity(jwtConfig = appConfig.jwt)
    configureSerialization()
    configureRouting(applicationGraph)
}

private fun ApplicationConfig.toAppConfig(): AppConfig =
    AppConfig(
        isDevelopment = property("ktor.development").getString().toBoolean(),
        jwt = config("jwt").jwtConfig,
        database = config("database").databaseConfig,
    )

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
