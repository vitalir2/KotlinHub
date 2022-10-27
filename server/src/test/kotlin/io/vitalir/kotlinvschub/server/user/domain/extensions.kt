package io.vitalir.kotlinvschub.server.user.domain

import io.kotest.matchers.shouldBe
import io.vitalir.kotlinvcshub.server.user.domain.model.User

internal val User.Companion.any: User
    get() = User(
        id = 123,
        login = "anyvalid",
        password = "anothervalid",
    )

internal infix fun User.shouldHaveTheSameCredentialsAs(other: User) {
    this.login shouldBe other.login
    this.email shouldBe other.email
    this.password shouldBe other.password
}
