package io.vitalir.kotlinhub.server.app.infrastructure.http

data class AuthorizationHeader(
    val regex: Regex,
) {

    fun valueFromHeader(headerValue: String): String? {
        return regex.find(headerValue)?.groupValues?.getOrNull(1)
    }

    companion object {
        val BASIC = AuthorizationHeader(regex = Regex("Basic (.+)"))
    }
}
