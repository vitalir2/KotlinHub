package io.vitalir.kotlinhub.server.app.infrastructure.routing.impl

import io.vitalir.kotlinhub.server.app.infrastructure.encoding.Base64Manager
import io.vitalir.kotlinhub.server.app.infrastructure.routing.BaseAuthValue
import io.vitalir.kotlinhub.server.app.infrastructure.routing.HeaderManager

internal class BaseAuthHeaderManager(
    private val base64Manager: Base64Manager,
) : HeaderManager<BaseAuthValue> {

    override fun parse(headerValue: String): BaseAuthValue {
        val credentials = base64Manager.decode(headerValue)
        val (username, password) = credentials.split(":")
        return BaseAuthValue(username = username, password = password)
    }

    override fun create(value: BaseAuthValue): String {
        val (username, password) = value
        val credentials = listOf(username, password).joinToString(":")
        return base64Manager.encode(credentials)
    }
}
