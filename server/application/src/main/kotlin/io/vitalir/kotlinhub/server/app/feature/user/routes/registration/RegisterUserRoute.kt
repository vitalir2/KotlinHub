package io.vitalir.kotlinhub.server.app.feature.user.routes.registration

import arrow.core.Either
import io.bkbn.kompendium.core.metadata.PostInfo
import io.bkbn.kompendium.core.plugin.NotarizedRoute
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.extensions.respondWith
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserCredentials
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserError
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.RegisterUserUseCase
import io.vitalir.kotlinhub.server.app.feature.user.routes.common.createToken
import io.vitalir.kotlinhub.server.app.feature.user.routes.getErrorResponseData
import io.vitalir.kotlinhub.server.app.infrastructure.config.AppConfig
import io.vitalir.kotlinhub.server.app.infrastructure.docs.badRequestResponse
import io.vitalir.kotlinhub.server.app.infrastructure.docs.reqType
import io.vitalir.kotlinhub.server.app.infrastructure.docs.resType
import io.vitalir.kotlinhub.shared.feature.user.RegisterUserRequest
import io.vitalir.kotlinhub.shared.feature.user.RegisterUserResponse

internal fun Route.registerUserRoute(
    registerUserUseCase: RegisterUserUseCase,
    jwtConfig: AppConfig.Auth.Jwt,
) {
    post {
        val registerUserRequest = call.receive<RegisterUserRequest>()
        val registrationResult = registerUserUseCase(
            UserCredentials(
                identifier = UserIdentifier.Username(registerUserRequest.login),
                password = registerUserRequest.password,
            )
        )
        val responseData: ResponseData = getResponseData(registrationResult, jwtConfig)
        call.respondWith(responseData)
    }
}

internal fun NotarizedRoute.Config.registerDocs() {
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
        badRequestResponse()
    }
}

private fun getResponseData(
    registrationResult: Either<UserError, User>,
    jwtConfig: AppConfig.Auth.Jwt,
): ResponseData {
    return when (registrationResult) {
        is Either.Left -> {
            getErrorResponseData(registrationResult.value)
        }

        is Either.Right -> {
            val responseBody = RegisterUserResponse(
                token = createToken(jwtConfig, registrationResult.value.id),
            )
            ResponseData(code = HttpStatusCode.Created, body = responseBody)
        }
    }
}
