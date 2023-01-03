package io.vitalir.web.data

import io.vitalir.kotlinhub.shared.feature.user.UserId
import io.vitalir.web.pages.main.models.Repository

interface RepositoriesRepository {

    suspend fun get(userId: UserId): Result<List<Repository>>
}
