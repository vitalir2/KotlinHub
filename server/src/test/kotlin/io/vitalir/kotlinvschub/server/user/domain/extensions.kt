package io.vitalir.kotlinvschub.server.user.domain

import io.vitalir.kotlinvcshub.server.user.domain.model.User

val User.Companion.any: User
    get() = User(
        id = 123,
        login = "anyvalid",
        password = "anothervalid",
    )
