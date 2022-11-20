package io.vitalir.kotlinhub.server.app.feature.user.routes.get

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.bkbn.kompendium.core.metadata.GetInfo
import io.bkbn.kompendium.core.plugin.NotarizedRoute
import io.bkbn.kompendium.json.schema.definition.TypeDefinition
import io.bkbn.kompendium.oas.payload.Parameter
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.extensions.respondWith
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.GetUserByIdentifierUseCase
import io.vitalir.kotlinhub.server.app.feature.user.routes.common.extensions.asApiUser
import io.vitalir.kotlinhub.server.app.feature.user.routes.common.usersTag
import io.vitalir.kotlinhub.server.app.infrastructure.auth.requireParameter
import io.vitalir.kotlinhub.server.app.infrastructure.docs.badRequestResponse
import io.vitalir.kotlinhub.server.app.infrastructure.docs.kompendiumDocs
import io.vitalir.kotlinhub.server.app.infrastructure.docs.resType

internal fun Route.userByIdentifierRoute(
    getUserByIdentifierUseCase: GetUserByIdentifierUseCase,
) {
    route("/{identifier}") {
        kompendiumDocs {
            usersTag()
            getUserByIdentifierDocs()
        }
        get {
            val identifierFromPath = call.requireParameter("identifier")
            val identifierType = call.request.queryParameters["type"]
            val parseResult = parseIdentifier(identifierFromPath = identifierFromPath, type = identifierType)
            if (parseResult is Either.Left) {
                call.respondWith(parseResult.value)
                return@get
            }

            // Cannot fail
            val identifier = (parseResult as Either.Right).value
            val user = getUserByIdentifierUseCase(identifier)

            call.respondWith(user.toResponseData())

        }
    }
}

private fun NotarizedRoute.Config.getUserByIdentifierDocs() {
    get = GetInfo.builder {
        summary("Get user by identifier")
        description("Get user by identifier - userId, username or email")
        parameters = listOf(
            Parameter(
                name = "identifier",
                `in` = Parameter.Location.path,
                schema = TypeDefinition.STRING,
                required = true,
            ),
            Parameter(
                name = "type",
                `in` = Parameter.Location.query,
                schema = TypeDefinition.STRING,
                required = false,
                description = "type of identifier. If not specified - username assumed",
            ),
        )
        response {
            resType<GetUserByLoginResponse>()
            responseCode(HttpStatusCode.OK)
            description("OK")
        }
        badRequestResponse()
    }
}

private fun parseIdentifier(
    identifierFromPath: String,
    type: String?,
): Either<ResponseData, UserIdentifier> {
    return when (type) {
        // Default - username
        null -> UserIdentifier.Username(identifierFromPath).right()
        "id" -> UserIdentifier.Id(identifierFromPath.toInt()).right()
        "username" -> UserIdentifier.Username(identifierFromPath).right()
        "email" -> UserIdentifier.Email(identifierFromPath).right()
        else -> ResponseData.badRequest("invalid identifier type=$type").left()
    }
}

private fun User?.toResponseData(): ResponseData {
    return if (this != null) {
        ResponseData(
            code = HttpStatusCode.OK,
            body = GetUserByLoginResponse(user = asApiUser),
        )
    } else {
        ResponseData.fromErrorData(
            code = HttpStatusCode.NotFound,
            errorMessage = "user not found",
        )
    }
}
