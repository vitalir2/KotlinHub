package io.vitalir.kotlinhub.server.app.infrastructure.encoding.impl

import io.ktor.util.*
import io.vitalir.kotlinhub.server.app.infrastructure.encoding.Base64Manager

internal class KtorBase64Manager : Base64Manager {

    override fun encode(rawString: String): String {
        return rawString.encodeBase64()
    }

    override fun decode(base64String: String): String {
        return base64String.decodeBase64String()
    }
}
