package io.vitalir.kotlinhub.server.app.feature.repository

import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.user.any
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import java.time.LocalDateTime

internal val Repository.Companion.any: Repository
    get() = Repository(
        id = 123,
        owner = User.any,
        name = "anyrepositoryname",
        accessMode = Repository.AccessMode.PRIVATE,
        createdAt = LocalDateTime.MIN,
        updatedAt = LocalDateTime.MIN,
    )
