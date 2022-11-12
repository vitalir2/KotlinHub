package io.vitalir.kotlinhub.server.app.feature.repository.data

import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinvschub.server.infrastructure.database.sqldelight.GetRepositoryByUsernameAndRepositoryName

internal fun GetRepositoryByUsernameAndRepositoryName.toDomainModel(): Repository {
    return Repository(
        owner = User(
            id = user_id!!,
            username = username,
            password = password,
            email = email,
        ),
        name = name,
        accessMode = access_mode.asAccessMode(),
        createdAt = created_at,
        updatedAt = updated_at,
        description = description,
    )
}

internal fun Int.asAccessMode(): Repository.AccessMode {
    return Repository.AccessMode.values()
        .first { it.asInt() == this }
}

internal fun Repository.AccessMode.asInt(): Int {
    return when (this) {
        Repository.AccessMode.PUBLIC -> 0
        Repository.AccessMode.PRIVATE -> 1
    }
}
