package io.vitalir.kotlinhub.server.app.feature.user.domain.usecase

import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier

interface GetUserByIdentifierUseCase {

    suspend operator fun invoke(userIdentifier: UserIdentifier): User?
}
