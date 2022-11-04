package io.vitalir.kotlinhub.server.app.user.routes.common.extensions

import io.vitalir.kotlinhub.server.app.user.domain.model.User
import io.vitalir.kotlinhub.server.app.user.routes.common.PureUser

internal val User.asPureUser: PureUser
    get() = PureUser(
        id = id,
        login = login,
        firstName = firstName,
        lastName = lastName,
        email = email,
    )
