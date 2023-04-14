package io.vitalir.kotlinhub.shared.feature.user

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@Serializable
@JsExport
class RegisterUserRequest(
    val login: String,
    val password: String,
)
