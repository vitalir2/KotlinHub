package io.vitalir.kotlinhub.server.app.feature.user.domain.model

sealed interface UserIdentifier {

    @JvmInline
    value class Id(val value: UserId) : UserIdentifier

    @JvmInline
    value class Login(val value: String) : UserIdentifier

    @JvmInline
    value class Email(val value: String) : UserIdentifier
}
