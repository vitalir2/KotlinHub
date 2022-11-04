package io.vitalir.kotlinhub.server.app.common.domain

import java.time.LocalDateTime

interface LocalDateTimeProvider {

    fun now(): LocalDateTime
}
