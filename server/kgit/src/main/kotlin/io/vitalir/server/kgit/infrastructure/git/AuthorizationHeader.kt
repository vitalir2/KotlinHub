package io.vitalir.server.kgit.infrastructure.git

data class AuthorizationHeader(
    val regex: Regex,
    val name: String,
) {

    fun valueFromHeader(headerValue: String): String? {
        return regex.find(headerValue)?.groupValues?.getOrNull(1)
    }

    companion object {
        val BASIC = AuthorizationHeader(
            regex = Regex("Basic (.+)"),
            name = "Basic",
        )
    }
}
