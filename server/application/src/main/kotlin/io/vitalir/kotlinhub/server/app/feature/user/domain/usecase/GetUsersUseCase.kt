package io.vitalir.kotlinhub.server.app.feature.user.domain.usecase

import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User

interface GetUsersUseCase {

    suspend operator fun invoke(): List<User>
}
