package io.vitalir.kotlinvcshub.server.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.routing.*
import io.ktor.server.webjars.*

fun Application.configureRouting() {

    install(StatusPages) {

    }
    install(Webjars) {
        path = "/webjars" //defaults to /webjars
    }
    routing {
    }
}
