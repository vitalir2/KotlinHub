package io.vitalir.kotlinhub.server.app.infrastructure.security

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import io.vitalir.kotlinhub.server.app.infrastructure.config.AppConfig

fun Application.configureSecurity(appConfig: AppConfig) {
    install(CORS) {
        if (appConfig.isDevelopmentMode) {
            anyHost()
        } else {
            allowHost("localhost:3000") // TODO pass from config
        }
        allowMethod(HttpMethod.Options)

        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.UserAgent)
    }
}
