package io.vitalir.kotlinhub.server.app.common.data

import io.vitalir.kotlinhub.server.app.common.domain.LocalDateTimeProvider
import java.time.LocalDateTime

internal class JavaLocalDateTimeProvider : LocalDateTimeProvider {

    override fun now(): LocalDateTime {
        return LocalDateTime.now()
    }
}
