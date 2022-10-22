package io.vitalir.kotlinvcshub.server

import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.netty.*
import io.vitalir.kotlinvcshub.server.infrastructure.config.AppConfig
import io.vitalir.kotlinvcshub.server.infrastructure.di.ApplicationGraphFactory
import io.vitalir.kotlinvcshub.server.infrastructure.di.ApplicationGraphFactoryImpl
import io.vitalir.kotlinvcshub.server.plugins.configureRouting
import io.vitalir.kotlinvcshub.server.plugins.configureSecurity
import io.vitalir.kotlinvcshub.server.plugins.configureSerialization

fun main(args: Array<String>) = EngineMain.main(args)

// Used in application config
@Suppress("UNUSED")
fun Application.mainModule() {
    val appConfig = environment.config.toAppConfig()
    val applicationGraphFactory: ApplicationGraphFactory = ApplicationGraphFactoryImpl()
    val applicationGraph = applicationGraphFactory.create(appConfig)
    configureSecurity(jwtConfig = appConfig.jwt)
    configureSerialization()
    configureRouting()
}

private fun ApplicationConfig.toAppConfig(): AppConfig =
    AppConfig(
        jwt = jwtConfig,
        database = databaseConfig,
    )

private val ApplicationConfig.jwtConfig: AppConfig.Jwt
    get() = AppConfig.Jwt(
        secret = property("jwt.secret").getString(),
        issuer = property("jwt.issuer").getString(),
        audience = property("jwt.audience").getString(),
        realm = property("jwt.realm").getString(),
    )

private val ApplicationConfig.databaseConfig: AppConfig.Database
    get() = AppConfig.Database(
        username = property("database.username").getString(),
        password = property("database.password").getString(),
    )
