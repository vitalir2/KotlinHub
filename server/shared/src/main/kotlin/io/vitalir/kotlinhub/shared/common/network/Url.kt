package io.vitalir.kotlinhub.shared.common.network

import kotlin.text.StringBuilder

class Url(
    val scheme: Scheme,
    val host: String,
    val path: Path,
    val port: Int? = null,
    val queryParams: Map<String, String> = emptyMap(),
) {

    override fun toString(): String {
        val urlValue = buildString {
            append(scheme.stringValue, "://", host)
            if (port != null) {
                append(':', port)
            }
            append("/", path)
            if (queryParams.isNotEmpty()) {
                appendQueryParams(queryParams)
            }
        }
        return urlValue
    }

    companion object {
        private fun StringBuilder.appendQueryParams(queryParams: Map<String, String>) {
            append("?")
            val queryString = queryParams
                .map { (name, value) -> "$name=$value" }
                .joinToString(",")
            append(queryString)
        }
    }
}
