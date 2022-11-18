package io.vitalir.kotlinhub.server.app.feature.user.routes.common.extensions

import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinhub.server.app.feature.user.routes.common.ApiUser

internal val User.asApiUser: ApiUser
    get() = ApiUser(
        id = id,
        username = username,
        firstName = firstName,
        lastName = lastName,
        email = email,
    )
