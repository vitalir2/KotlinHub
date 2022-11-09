package io.vitalir.kotlinhub.server.app.infrastructure.encoding

interface Base64Manager {

    fun encode(rawString: String): String

    fun decode(base64String: String): String
}
