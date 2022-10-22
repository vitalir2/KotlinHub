package io.vitalir.kotlinvcshub.server.infrastructure.di

import io.vitalir.kotlinvcshub.server.infrastructure.config.AppConfig

interface ApplicationGraphFactory {

    fun create(
        appConfig: AppConfig,
    ): ApplicationGraph
}
