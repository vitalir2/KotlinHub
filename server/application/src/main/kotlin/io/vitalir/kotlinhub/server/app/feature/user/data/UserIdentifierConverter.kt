package io.vitalir.kotlinhub.server.app.feature.user.data

import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.infrastructure.database.sqldelight.MainSqlDelight
import io.vitalir.kotlinhub.shared.feature.user.UserId
import io.vitalir.kotlinvschub.server.infrastructure.database.sqldelight.CUsersQueries

internal class UserIdentifierConverter(
    private val mainSqlDelight: MainSqlDelight,
) {

    private val userQueries: CUsersQueries
        get() = mainSqlDelight.cUsersQueries

    fun convertToUserId(userIdentifier: UserIdentifier): UserId {
        return when (userIdentifier) {
            is UserIdentifier.Email -> userQueries.getUserIdByEmail(userIdentifier.value).executeAsOne()
            is UserIdentifier.Id -> userIdentifier.value
            is UserIdentifier.Username -> userQueries.getUserIdByUsername(userIdentifier.value).executeAsOne()
        }
    }
}
