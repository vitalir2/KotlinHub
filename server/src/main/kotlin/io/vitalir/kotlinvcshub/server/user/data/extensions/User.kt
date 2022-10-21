package io.vitalir.kotlinvcshub.server.user.data.extensions

import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvschub.server.infrastructure.database.sqldelight.Users

internal fun Users.toDomainModel(): User = User(
    id = id,
    login = login,
    password = password,
    email = email,
)
