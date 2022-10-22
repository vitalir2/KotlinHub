package io.vitalir.kotlinvcshub.server.infrastructure.di

import io.vitalir.kotlinvcshub.server.infrastructure.config.AppConfig

interface AppGraphFactory {

    fun create(
        appConfig: AppConfig,
    ): AppGraph
}
