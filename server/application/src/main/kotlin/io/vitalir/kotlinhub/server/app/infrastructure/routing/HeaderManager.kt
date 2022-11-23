package io.vitalir.kotlinhub.server.app.infrastructure.routing

interface HeaderManager<T> {

    fun parse(headerValue: String): T

    fun create(value: T): String
}
