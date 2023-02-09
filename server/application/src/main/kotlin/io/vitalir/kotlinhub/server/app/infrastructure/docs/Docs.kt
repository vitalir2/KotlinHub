package io.vitalir.kotlinhub.server.app.infrastructure.docs

import io.bkbn.kompendium.core.attribute.KompendiumAttributes
import io.bkbn.kompendium.core.metadata.MethodInfo
import io.bkbn.kompendium.core.metadata.RequestInfo
import io.bkbn.kompendium.core.metadata.ResponseInfo
import io.bkbn.kompendium.core.plugin.NotarizedApplication
import io.bkbn.kompendium.core.plugin.NotarizedRoute
import io.bkbn.kompendium.json.schema.KotlinXSchemaConfigurator
import io.bkbn.kompendium.json.schema.definition.TypeDefinition
import io.bkbn.kompendium.oas.OpenApiSpec
import io.bkbn.kompendium.oas.component.Components
import io.bkbn.kompendium.oas.info.Info
import io.bkbn.kompendium.oas.security.BearerAuth
import io.bkbn.kompendium.oas.serialization.KompendiumSerializersModule
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import io.vitalir.kotlinhub.server.app.common.routes.AuthVariant
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.extensions.respondWith
import io.vitalir.kotlinhub.server.app.common.routes.jwtAuth
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.infrastructure.auth.userId
import io.vitalir.kotlinhub.server.app.infrastructure.di.AppGraph
import io.vitalir.kotlinhub.shared.common.ErrorResponse
import io.vitalir.kotlinhub.shared.common.KMPLocalDateTime
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

@OptIn(ExperimentalSerializationApi::class)
private val kompendiumJson = Json {
    serializersModule = KompendiumSerializersModule.module
    encodeDefaults = true
    explicitNulls = false
}

internal fun Application.configureDocs(appGraph: AppGraph) {
    install(NotarizedApplication()) {
        spec = OpenApiSpec(
            info = Info(
                title = "KotlinHub",
                version = "0.0.1",
                description = "KotlinHub is a Git repositories storage platform",
            ),
            components = Components(
                securitySchemes = mutableMapOf(
                    AuthVariant.JWT.authName to BearerAuth(),
                ),
            ),
        )
        customTypes = mapOf(
            typeOf<KMPLocalDateTime>() to TypeDefinition(type = "string", format = "date-time"),
        )
        openApiJson = createOpenAPIJsonRouting(appGraph)
        schemaConfigurator = KotlinXSchemaConfigurator()
    }
}

private fun createOpenAPIJsonRouting(appGraph: AppGraph): Routing.() -> Unit {
    return {
        route("/openapi.json") {
            if (appGraph.appConfig.debug?.isDocsEnabled == true) {
                get {
                    respondOpenAPIJson()
                }
            } else {
                jwtAuth {
                    get {
                        val userId = call.userId
                        // If performance issues - create isAdminUseCase or save this info in JWT ?
                        val user = appGraph.user.getUserByIdentifierUseCase(UserIdentifier.Id(userId))
                        if (user?.isAdmin == false) {
                            call.respondWith(ResponseData.forbidden())
                            return@get
                        }

                        respondOpenAPIJson()
                    }
                }
            }
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.respondOpenAPIJson() {
    val stringSpec =
        kompendiumJson.encodeToString(application.attributes[KompendiumAttributes.openApiSpec])
    call.respond(HttpStatusCode.OK, stringSpec)
}

internal fun Route.kompendiumDocs(
    block: NotarizedRoute.Config.() -> Unit,
) {
    install(NotarizedRoute(), block)
}

internal fun MethodInfo.Builder<*>.badRequestResponse() {
    canRespond {
        resType<ErrorResponse>()
        responseCode(HttpStatusCode.BadRequest)
        description("Error, see 'message' for more details")
    }
}

internal fun MethodInfo.Builder<*>.serverErrorResponse() {
    canRespond {
        resType<ErrorResponse>()
        responseCode(HttpStatusCode.InternalServerError)
        description("Server error")
    }
}

internal inline fun <reified T> RequestInfo.Builder.reqType() {
    requestType(typeOf<T>())
}

internal inline fun <reified T> ResponseInfo.Builder.resType() {
    responseType(typeOf<T>())
}
