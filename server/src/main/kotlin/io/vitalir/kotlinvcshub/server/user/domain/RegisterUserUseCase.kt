package io.vitalir.kotlinvcshub.server.user.domain

interface RegisterUserUseCase {

    operator fun invoke(credentials: User.Credentials): User
}
