package io.vitalir.kotlinvcshub.server.common.domain

import java.time.LocalDateTime

interface LocalDateTimeProvider {

    fun now(): LocalDateTime
}
