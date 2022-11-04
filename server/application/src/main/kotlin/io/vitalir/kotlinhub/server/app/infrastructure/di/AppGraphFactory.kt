package io.vitalir.kotlinhub.server.app.infrastructure.di

import io.vitalir.kotlinhub.server.app.infrastructure.config.AppConfig

interface AppGraphFactory {

    fun create(
        appConfig: AppConfig,
    ): AppGraph
}
