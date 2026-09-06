// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
package app.gauja.core.data.servers

import java.util.UUID
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

internal fun probeJson(): Json = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
    serializersModule = SerializersModule { contextual(UUID::class, WireUuid) }
}

// The generated OpenAPI UUID fields use a contextual java.util.UUID serializer.
private object WireUuid : KSerializer<UUID> {
    override val descriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: UUID) = encoder.encodeString(value.toString())

    override fun deserialize(decoder: Decoder): UUID =
        try {
            UUID.fromString(decoder.decodeString())
        } catch (_: IllegalArgumentException) {
            throw SerializationException("Invalid UUID")
        }
}
