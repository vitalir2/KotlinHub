package io.vitalir.kotlinvcshub.server.user.domain

interface LoginUseCase {

    operator fun invoke(credentials: User.Credentials): User
}
