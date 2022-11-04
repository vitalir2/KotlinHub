package io.vitalir.kotlinvcshub.server.user.routes.common.extensions

import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvcshub.server.user.routes.common.PureUser

internal val User.asPureUser: PureUser
    get() = PureUser(
        id = id,
        login = login,
        firstName = firstName,
        lastName = lastName,
        email = email,
    )
