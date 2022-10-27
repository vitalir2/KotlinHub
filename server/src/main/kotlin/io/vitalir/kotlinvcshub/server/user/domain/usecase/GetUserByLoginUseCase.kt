package io.vitalir.kotlinvcshub.server.user.domain.usecase

import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvcshub.server.user.domain.model.UserCredentials

interface GetUserByLoginUseCase {

    operator fun invoke(login: UserCredentials.Identifier.Login): User?
}
