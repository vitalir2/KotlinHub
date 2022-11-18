package io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase

import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import java.time.LocalDateTime

// TODO we cannot update repository name for now because it mirrors file system git repository path,
//  therefore the update will break git actions.
//  To fix it, we need to build a proxy between app / kgit server that maps repository name to its id
data class UpdateRepositoryData(
    val accessMode: Repository.AccessMode? = null,
    val updatedAt: Time? = null,
) {

    sealed interface Time {
        object Now : Time
        @JvmInline
        value class Custom(val time: LocalDateTime) : Time
    }
}
