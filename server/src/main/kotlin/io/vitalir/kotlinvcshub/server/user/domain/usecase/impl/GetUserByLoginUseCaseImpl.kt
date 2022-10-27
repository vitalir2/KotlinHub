package io.vitalir.kotlinvcshub.server.user.domain.usecase.impl

import io.vitalir.kotlinvcshub.server.user.domain.model.User
import io.vitalir.kotlinvcshub.server.user.domain.model.UserCredentials
import io.vitalir.kotlinvcshub.server.user.domain.persistence.UserPersistence
import io.vitalir.kotlinvcshub.server.user.domain.usecase.GetUserByLoginUseCase

internal class GetUserByLoginUseCaseImpl(
    private val userPersistence: UserPersistence,
) : GetUserByLoginUseCase {

    override suspend fun invoke(login: UserCredentials.Identifier.Login): User? {
        return userPersistence.getUser(login).orNull()
    }
}