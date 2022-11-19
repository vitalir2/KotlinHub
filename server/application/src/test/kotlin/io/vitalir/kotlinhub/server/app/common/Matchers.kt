package io.vitalir.kotlinhub.server.app.common

import arrow.core.Either
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.beOfType
import kotlin.reflect.KClass

internal infix fun Either<*, *>.shouldBeLeftWithType(expected: KClass<*>) {
    this.shouldBeLeft()
    this.value shouldBe beOfType(expected)
}

internal inline fun <reified U : Any> Either<*, *>.shouldBeLeftWithType() {
    this.shouldBeLeftWithType(U::class)
}
