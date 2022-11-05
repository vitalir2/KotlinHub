package io.vitalir.server.kgit.client

data class Response(
    val code: HttpCode,
) {

    enum class HttpCode(val number: Int) {
        OK(200),
    }
}
