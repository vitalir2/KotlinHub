package io.vitalir.kotlinvcshub.server.repository.domain.model

import io.vitalir.kotlinvcshub.server.common.domain.LocalDateTimeProvider
import io.vitalir.kotlinvcshub.server.user.domain.model.UserId
import java.time.LocalDateTime

data class Repository(
    val ownerId: UserId,
    val name: String,
    val accessMode: AccessMode,
    val createdAt: LocalDateTime,
    val lastUpdated: LocalDateTime,
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
           initData: CreateRepositoryData,
           localDateTimeProvider: LocalDateTimeProvider,
       ): Repository {
           val createdAtDateTime = localDateTimeProvider.now()
           return Repository(
               ownerId = initData.userId,
               name = initData.name,
               accessMode = initData.accessMode,
               description = initData.description,
               createdAt = createdAtDateTime,
               lastUpdated = createdAtDateTime,
           )
       }
    }
}
