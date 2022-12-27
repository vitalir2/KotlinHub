@file:OptIn(ExperimentalJsExport::class)

package io.vitalir.kotlinhub.shared.common

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalSerializationApi::class)
fun createHttpClient(
    baseUrl: String,
): HttpClient = HttpClient {
    defaultRequest {
        url(baseUrl)
    }
    install(ContentNegotiation) {
        json(
            Json {
               explicitNulls = false
            }
        )
    }
}

@JsExport
fun kek(): String {
    return "kek"
}
