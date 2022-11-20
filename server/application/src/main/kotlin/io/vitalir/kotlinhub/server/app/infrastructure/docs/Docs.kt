package io.vitalir.kotlinhub.server.app.infrastructure.docs

import io.bkbn.kompendium.core.attribute.KompendiumAttributes
import io.bkbn.kompendium.core.metadata.MethodInfo
import io.bkbn.kompendium.core.metadata.RequestInfo
import io.bkbn.kompendium.core.metadata.ResponseInfo
import io.bkbn.kompendium.core.plugin.NotarizedApplication
import io.bkbn.kompendium.core.plugin.NotarizedRoute
import io.bkbn.kompendium.json.schema.KotlinXSchemaConfigurator
import io.bkbn.kompendium.oas.OpenApiSpec
import io.bkbn.kompendium.oas.component.Components
import io.bkbn.kompendium.oas.info.Info
import io.bkbn.kompendium.oas.security.BearerAuth
import io.bkbn.kompendium.oas.serialization.KompendiumSerializersModule
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.AuthVariant
import io.vitalir.kotlinhub.server.app.common.routes.ErrorResponse
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

internal fun Application.configureDocs() {
    install(NotarizedApplication()) {
        spec = OpenApiSpec(
            info = Info(
                title = "KotlinHub",
                version = "0.0.1",
                description = "KotlinHub is a TODO",
            ),
            components = Components(
                securitySchemes = mutableMapOf(
                    AuthVariant.JWT.authName to BearerAuth(),
                ),
            ),
        )
        openApiJson = {
            // TODO replace by user role when it will be implemented
            if (application.environment.developmentMode) {
                get("/openapi.json") {
                    val stringSpec = kompendiumJson.encodeToString(application.attributes[KompendiumAttributes.openApiSpec])
                    call.respond(HttpStatusCode.OK, stringSpec)
                }
            }
        }
        schemaConfigurator = KotlinXSchemaConfigurator()
    }
}

internal fun Route.kompendiumDocs(block: NotarizedRoute.Config.() -> Unit) {
    install(NotarizedRoute(), block)
}

internal fun MethodInfo.Builder<*>.badRequestResponse() {
    canRespond {
        resType<ErrorResponse>()
        responseCode(HttpStatusCode.BadRequest)
        description("Error, see 'message' for more details")
    }
}

internal inline fun <reified T> RequestInfo.Builder.reqType() {
    requestType(typeOf<T>())
}

internal inline fun <reified T> ResponseInfo.Builder.resType() {
    responseType(typeOf<T>())
}
