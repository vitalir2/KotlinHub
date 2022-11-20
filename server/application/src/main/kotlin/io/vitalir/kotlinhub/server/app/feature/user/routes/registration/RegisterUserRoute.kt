package io.vitalir.kotlinhub.server.app.feature.user.routes.registration

import arrow.core.Either
import io.bkbn.kompendium.core.metadata.PostInfo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.ErrorResponse
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.extensions.respondWith
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserCredentials
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserError
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.RegisterUserUseCase
import io.vitalir.kotlinhub.server.app.feature.user.routes.getErrorResponseData
import io.vitalir.kotlinhub.server.app.infrastructure.docs.kompendiumDocs
import io.vitalir.kotlinhub.server.app.infrastructure.docs.reqType
import io.vitalir.kotlinhub.server.app.infrastructure.docs.resType

internal fun Route.registerUserRoute(registerUserUseCase: RegisterUserUseCase) {
    post {
        val registerUserRequest = call.receive<RegisterUserRequest>()
        val registrationResult = registerUserUseCase(
            UserCredentials(
                identifier = UserIdentifier.Username(registerUserRequest.login),
                password = registerUserRequest.password,
            )
        )
        val responseData: ResponseData = getResponseData(registrationResult)
        call.respondWith(responseData)
    }
}

internal fun Route.registerDocs() {
    kompendiumDocs {
        post = PostInfo.builder {
            summary("Register user")
            description("Registers user if all credentials are valid")
            request {
                reqType<RegisterUserRequest>()
                description("Credentials")
            }
            response {
                resType<RegisterUserResponse>()
                responseCode(HttpStatusCode.Created)
                description("Registered user id")
            }
            canRespond {
                resType<ErrorResponse>()
                responseCode(HttpStatusCode.BadRequest)
                description("Error, see 'message' for more details")
            }
        }
    }
}

private fun getResponseData(registrationResult: Either<UserError, User>): ResponseData {
    return when (registrationResult) {
        is Either.Left -> {
            getErrorResponseData(registrationResult.value)
        }
        is Either.Right -> {
            val responseBody = RegisterUserResponse(userId = registrationResult.value.id)
            ResponseData(code = HttpStatusCode.Created, body = responseBody)
        }
    }
}
