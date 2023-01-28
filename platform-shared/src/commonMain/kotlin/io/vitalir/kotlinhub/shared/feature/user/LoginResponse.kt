package io.vitalir.kotlinhub.shared.feature.user

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@Serializable
@JsExport
class LoginResponse(
    val token: String,
)
