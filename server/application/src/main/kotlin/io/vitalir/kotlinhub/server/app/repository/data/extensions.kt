package io.vitalir.kotlinhub.server.app.repository.data

import io.vitalir.kotlinhub.server.app.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.user.domain.model.User
import io.vitalir.kotlinvschub.server.infrastructure.database.sqldelight.GetRepositoryByUsernameAndRepositoryName

internal fun GetRepositoryByUsernameAndRepositoryName.toDomainModel(): Repository {
    return Repository(
        owner = User(
            id = user_id!!,
            login = login,
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
    return when (this) {
        0 -> Repository.AccessMode.PUBLIC
        1 -> Repository.AccessMode.PRIVATE
        else -> error("TODO")
    }
}
