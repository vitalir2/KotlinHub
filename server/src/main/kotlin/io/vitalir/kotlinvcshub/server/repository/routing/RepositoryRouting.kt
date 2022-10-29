package io.vitalir.kotlinvcshub.server.repository.routing

import arrow.core.Either
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.vitalir.kotlinvcshub.server.common.routes.AuthVariant
import io.vitalir.kotlinvcshub.server.common.routes.ResponseData
import io.vitalir.kotlinvcshub.server.common.routes.extensions.respondByResponseData
import io.vitalir.kotlinvcshub.server.infrastructure.auth.userId
import io.vitalir.kotlinvcshub.server.infrastructure.di.AppGraph
import io.vitalir.kotlinvcshub.server.repository.domain.model.CreateRepositoryData
import io.vitalir.kotlinvcshub.server.repository.domain.model.RepositoryError
import io.vitalir.kotlinvcshub.server.repository.domain.usecase.CreateRepositoryUseCase

internal fun Routing.repositoryRoutes(
    repositoryGraph: AppGraph.RepositoryGraph,
) {
    route("repository/") {
        createRepositoryRoute(repositoryGraph.createRepositoryUseCase)
    }
}

private fun Route.createRepositoryRoute(
    createRepositoryUseCase: CreateRepositoryUseCase,
) {
    authenticate(AuthVariant.JWT.authName) {
        post {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.payload?.userId ?: run {
                call.respond(ResponseData.unauthorized())
                return@post
            }

            val request = call.receive<CreateRepositoryRequest>()
            val createRepositoryData = CreateRepositoryData(
                userId = userId,
                name = request.name,
                accessMode = request.accessMode,
                description = request.description,
            )

            val result = createRepositoryUseCase(createRepositoryData)
            val responseData = result.toCreateRepositoryResponseData()
            call.respondByResponseData(responseData)
        }
    }
}

private fun Either<RepositoryError.Create, Unit>.toCreateRepositoryResponseData(): ResponseData {
    return when (this) {
        is Either.Left -> value.toResponseData()
        is Either.Right -> ResponseData(
            code = HttpStatusCode.Created,
            body = CreateRepositoryResponse(repositoryUrl = "TODO"),
        )
    }
}

private fun RepositoryError.Create.toResponseData(): ResponseData {
    return when (this) {
        is RepositoryError.Create.InvalidUserId -> ResponseData.fromErrorData(
            code = HttpStatusCode.Unauthorized,
            errorMessage = "unauthorized",
        )
        is RepositoryError.Create.RepositoryAlreadyExists -> ResponseData.fromErrorData(
            code = HttpStatusCode.BadRequest,
            errorMessage = "repository already exists",
        )
    }
}
