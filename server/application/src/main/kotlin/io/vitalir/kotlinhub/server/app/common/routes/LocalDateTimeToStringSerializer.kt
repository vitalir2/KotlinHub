package io.vitalir.kotlinhub.server.app.common.routes

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object LocalDateTimeToStringSerializer : KSerializer<LocalDateTime> {

    private val dateFormatter = DateTimeFormatter.ISO_DATE_TIME

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        val string = value.format(dateFormatter)
        encoder.encodeString(string)
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        val string = decoder.decodeString()
        return LocalDateTime.parse(string, dateFormatter)
    }
}
