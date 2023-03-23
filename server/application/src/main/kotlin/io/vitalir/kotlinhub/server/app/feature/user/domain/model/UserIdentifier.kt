package io.vitalir.kotlinhub.server.app.feature.user.domain.model

import io.vitalir.kotlinhub.shared.feature.user.UserId

// TODO think about simplification of identifiers e.g. use only ID
sealed interface UserIdentifier {

    @JvmInline
    value class Id(val value: UserId) : UserIdentifier

    @JvmInline
    value class Username(val value: String) : UserIdentifier

    @JvmInline
    value class Email(val value: String) : UserIdentifier
}
