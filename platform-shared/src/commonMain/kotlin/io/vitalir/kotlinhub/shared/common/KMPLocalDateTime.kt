package io.vitalir.kotlinhub.shared.common

import kotlinx.datetime.LocalDateTime
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
) {

    private val mDateTime = LocalDateTime(
        year = year,
        monthNumber = monthNumber,
        dayOfMonth = dayOfMonth,
        hour = hour,
        minute = minute,
        second = second,
    )

    override fun toString(): String {
        return mDateTime.toString()
    }
}

fun LocalDateTime.toKMPModel() = KMPLocalDateTime(
    year = year,
    monthNumber = monthNumber,
    dayOfMonth = dayOfMonth,
    hour = hour,
    minute = minute,
    second = second,
)
