package io.vitalir.kotlinhub.server.app.feature.user.routes.get

import io.bkbn.kompendium.core.metadata.GetInfo
import io.bkbn.kompendium.core.plugin.NotarizedRoute
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.extensions.respondWith
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.GetUsersUseCase
import io.vitalir.kotlinhub.server.app.feature.user.routes.common.extensions.asApiUser
import io.vitalir.kotlinhub.server.app.infrastructure.docs.resType

internal fun Route.getUsersRoute(
    getUsersUseCase: GetUsersUseCase,
) {
    get {
        val result = getUsersUseCase()
        val responseData = ResponseData(
            code = HttpStatusCode.OK,
            body = GetUsersResponse(
                users = result.map(User::asApiUser),
            ),
        )
        call.respondWith(responseData)
    }
}

internal fun NotarizedRoute.Config.getUsersDocs() {
    get = GetInfo.builder {
        summary("Get users")
        description("")
        response {
            resType<GetUsersResponse>()
            responseCode(HttpStatusCode.OK)
            description("OK")
        }
    }
}
