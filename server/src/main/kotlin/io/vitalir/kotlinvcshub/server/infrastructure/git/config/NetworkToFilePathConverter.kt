package io.vitalir.kotlinvcshub.server.infrastructure.git.config

internal fun interface NetworkToFilePathConverter {

    fun convert(url: String): String
}
