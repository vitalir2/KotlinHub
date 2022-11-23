package io.vitalir.kotlinhub.server.app.feature.repository.data

import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinvschub.server.infrastructure.database.sqldelight.GetRepositoryByIdJoined
import io.vitalir.kotlinvschub.server.infrastructure.database.sqldelight.GetRepositoryByUserIdAndNameJoined
import io.vitalir.kotlinvschub.server.infrastructure.database.sqldelight.Repositories

internal fun GetRepositoryByUserIdAndNameJoined.toDomainModel(): Repository {
    return Repository(
        id = id,
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

internal fun GetRepositoryByIdJoined.toDomainModel(): Repository {
    return Repository(
        id = id,
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

internal fun Repositories.toDomainModel(owner: User): Repository {
    return Repository(
        id = id,
        owner = owner,
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
