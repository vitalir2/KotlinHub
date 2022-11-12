package io.vitalir.kotlinhub.server.app.feature.user.routes.getuser

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.extensions.respondWith
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.GetUserByIdentifierUseCase
import io.vitalir.kotlinhub.server.app.feature.user.routes.common.extensions.asPureUser

internal fun Route.getUserByUsernameRoute(
    getUserByIdentifierUseCase: GetUserByIdentifierUseCase,
) {
    get("{login}/") {
        val login = call.parameters["login"] ?: run {
            call.respondWith(
                ResponseData.fromErrorData(
                    code = HttpStatusCode.BadRequest,
                    errorMessage = "invalid login",
                )
            )
            return@get
        }

        val user = getUserByIdentifierUseCase(UserIdentifier.Username(login))
        val resultResponseData = if (user != null) {
            ResponseData(
                code = HttpStatusCode.OK,
                body = GetUserByLoginResponse(user = user.asPureUser),
            )
        } else {
            ResponseData.fromErrorData(
                code = HttpStatusCode.NotFound,
                errorMessage = "user not found",
            )
        }
        call.respondWith(resultResponseData)
    }
}
