package io.vitalir.kotlinhub.shared.feature.user

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@Serializable
@JsExport
class ApiUser(
    val id: UserId,
    val username: String,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
)
