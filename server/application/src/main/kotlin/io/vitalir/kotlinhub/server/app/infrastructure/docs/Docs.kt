package io.vitalir.kotlinhub.server.app.infrastructure.docs

import io.bkbn.kompendium.core.attribute.KompendiumAttributes
import io.bkbn.kompendium.core.plugin.NotarizedApplication
import io.bkbn.kompendium.oas.OpenApiSpec
import io.bkbn.kompendium.oas.info.Info
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

internal fun Application.configureDocs() {
    install(NotarizedApplication()) {
        spec = OpenApiSpec(
            info = Info(
                title = "KotlinHub",
                version = "0.0.1",
            ),
        )
        openApiJson = {
            // TODO replace by user role when it will be implemented
            if (application.environment.developmentMode) {
                get("/openapi.json") {
                    call.respond(HttpStatusCode.OK, application.attributes[KompendiumAttributes.openApiSpec])
                }
            }
        }
    }
}
