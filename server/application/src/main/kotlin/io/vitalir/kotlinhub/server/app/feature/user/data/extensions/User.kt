package io.vitalir.kotlinhub.server.app.feature.user.data.extensions

import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinvschub.server.infrastructure.database.sqldelight.Users

internal fun Users.toDomainModel(): User = User(
    id = id,
    login = login,
    password = password,
    email = email,
)
