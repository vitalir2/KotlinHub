package io.vitalir.kotlinhub.server.app.repository.domain.model

import io.vitalir.kotlinhub.server.app.common.domain.LocalDateTimeProvider
import io.vitalir.kotlinhub.server.app.user.domain.model.User
import io.vitalir.kotlinhub.server.app.user.domain.model.UserId
import java.time.LocalDateTime

data class Repository(
    val owner: User,
    val name: String,
    val accessMode: AccessMode,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val description: String? = null,
    val commits: List<Commit> = emptyList(),
) {

    enum class AccessMode {
        PUBLIC,
        PRIVATE,
    }

    data class Commit(
        val author: UserId,
        val message: String,
    )

    companion object {
       fun fromInitData(
           owner: User,
           initData: CreateRepositoryData,
           localDateTimeProvider: LocalDateTimeProvider,
       ): Repository {
           val createdAtDateTime = localDateTimeProvider.now()
           return Repository(
               owner = owner,
               name = initData.name,
               accessMode = initData.accessMode,
               description = initData.description,
               createdAt = createdAtDateTime,
               updatedAt = createdAtDateTime,
           )
       }
    }
}