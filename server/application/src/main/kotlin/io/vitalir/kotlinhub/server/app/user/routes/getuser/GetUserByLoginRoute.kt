package io.vitalir.kotlinhub.server.app.user.routes.getuser

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.user.domain.model.UserCredentials
import io.vitalir.kotlinhub.server.app.user.domain.usecase.GetUserByLoginUseCase
import io.vitalir.kotlinhub.server.app.user.routes.common.extensions.asPureUser

internal fun Route.getUserByLoginRoute(
    getUserByLoginUseCase: GetUserByLoginUseCase,
) {
    get("{login}/") {
        val login = call.parameters["login"] ?: run {
            call.respond(
                ResponseData.fromErrorData(
                    code = HttpStatusCode.BadRequest,
                    errorMessage = "invalid login",
                )
            )
            return@get
        }

        val user = getUserByLoginUseCase(UserCredentials.Identifier.Login(login))
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
        call.respond(resultResponseData)
    }
}