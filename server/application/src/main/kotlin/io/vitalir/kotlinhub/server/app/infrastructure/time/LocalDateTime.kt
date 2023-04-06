package io.vitalir.kotlinhub.server.app.infrastructure.time

import io.vitalir.kotlinhub.shared.common.KMPLocalDateTime
import java.time.LocalDateTime

fun LocalDateTime.toKMPModel(): KMPLocalDateTime {
    return KMPLocalDateTime(
        year = year,
        monthNumber = monthValue,
        dayOfMonth = dayOfMonth,
        hour = hour,
        minute = minute,
        second = second,
    )
}
