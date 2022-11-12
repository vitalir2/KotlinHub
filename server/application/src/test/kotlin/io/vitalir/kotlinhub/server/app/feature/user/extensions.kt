package io.vitalir.kotlinhub.server.app.feature.user

import io.kotest.matchers.shouldBe
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User

internal val User.Companion.any: User
    get() = User(
        id = 123,
        username = "anyvalid",
        password = "anothervalid",
    )

internal infix fun User.shouldHaveTheSameCredentialsAs(other: User) {
    this.username shouldBe other.username
    this.email shouldBe other.email
    this.password shouldBe other.password
}
