package io.vitalir.kotlinhub.server.app

import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.netty.*
import io.vitalir.kotlinhub.server.app.infrastructure.auth.configureAuth
import io.vitalir.kotlinhub.server.app.infrastructure.config.AppConfig
import io.vitalir.kotlinhub.server.app.infrastructure.di.AppGraphFactory
import io.vitalir.kotlinhub.server.app.infrastructure.di.AppGraphFactoryImpl
import io.vitalir.kotlinhub.server.app.infrastructure.docs.configureDocs
import io.vitalir.kotlinhub.server.app.infrastructure.routing.configureRouting
import io.vitalir.kotlinhub.server.app.infrastructure.serialization.configureSerialization

fun main(args: Array<String>) = EngineMain.main(args)

// Used in application config
@Suppress("UNUSED")
fun Application.mainModule() {
    val appConfig = environment.config.toAppConfig()
    val appGraphFactory: AppGraphFactory = AppGraphFactoryImpl(this)
    val appGraph = appGraphFactory.create(appConfig)

    configureAuth(appGraph)
    configureSerialization()
    configureDocs(appGraph)
    configureRouting(appGraph)
}

private fun ApplicationConfig.toAppConfig(): AppConfig {
    val isDevelopmentMode = property("ktor.development").getString().toBoolean()
    return AppConfig(
        debug = if (isDevelopmentMode) config("debug").debugConfig else null,
        auth = authConfig,
        database = config("database").databaseConfig,
        repository = config("repository").repositoryConfig,
    )
}

private val ApplicationConfig.authConfig: AppConfig.Auth
    get() = AppConfig.Auth(
        jwt = config("jwt").jwtConfig,
        defaultAdmin = config("defaultAdmin").defaultAdminConfig,
    )

private val ApplicationConfig.jwtConfig: AppConfig.Auth.Jwt
    get() {
        return AppConfig.Auth.Jwt(
            secret = property("secret").getString(),
            issuer = property("issuer").getString(),
            audience = property("audience").getString(),
            realm = property("realm").getString(),
        )
    }

private val ApplicationConfig.defaultAdminConfig: AppConfig.Auth.DefaultAdmin
    get() {
        return AppConfig.Auth.DefaultAdmin(
            username = property("username").getString(),
            password = property("password").getString(),
        )
    }

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
        isDocsEnabled = property("docs").getString().toBoolean(),
    )

private val ApplicationConfig.repositoryConfig: AppConfig.Repository
    get() = AppConfig.Repository(
        baseRepositoriesPath = property("baseRepositoriesPath").getString(),
    )
