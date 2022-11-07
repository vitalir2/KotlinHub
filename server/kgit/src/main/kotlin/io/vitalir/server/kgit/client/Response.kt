package io.vitalir.server.kgit.client

data class Response(
    val code: HttpCode,
) {

    sealed interface HttpCode {
        object Unknown : HttpCode
        enum class Valid(val number: Int) : HttpCode {
            OK(200),
        }

        companion object {
            fun fromNumber(code: Int): HttpCode = HttpCode.Valid.values()
                .firstOrNull { it.number == code }
                ?: Unknown
        }
    }
}
