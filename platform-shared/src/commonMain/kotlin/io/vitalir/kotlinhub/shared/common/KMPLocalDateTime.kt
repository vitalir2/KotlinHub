package io.vitalir.kotlinhub.shared.common

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@Serializable
@JsExport
data class KMPLocalDateTime(
    val year: Int,
    val monthNumber: Int,
    val dayOfMonth: Int,
    val hour: Int,
    val minute: Int,
    val second: Int,
)
