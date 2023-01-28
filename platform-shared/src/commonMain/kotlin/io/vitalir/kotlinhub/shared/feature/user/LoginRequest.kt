package io.vitalir.kotlinhub.shared.feature.user

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@Serializable
@JsExport
class LoginRequest(
    val username: String,
    val password: String,
)
