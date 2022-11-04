package io.vitalir.kotlinhub.server.app.user.domain.usecase

import io.vitalir.kotlinhub.server.app.user.domain.model.User
import io.vitalir.kotlinhub.server.app.user.domain.model.UserCredentials

interface GetUserByLoginUseCase {

    suspend operator fun invoke(login: UserCredentials.Identifier.Login): User?
}
