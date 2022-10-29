package io.vitalir.kotlinvcshub.server.common.data

import io.vitalir.kotlinvcshub.server.common.domain.LocalDateTimeProvider
import java.time.LocalDateTime

internal class JavaLocalDateTimeProvider : LocalDateTimeProvider {

    override fun now(): LocalDateTime {
        return LocalDateTime.now()
    }
}
