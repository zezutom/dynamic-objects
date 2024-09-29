package com.tomaszezula.eventsourcing.model.api

import com.tomaszezula.eventsourcing.context.DynamicContext
import com.tomaszezula.eventsourcing.context.DynamicProperty
import com.tomaszezula.eventsourcing.model.Event
import com.tomaszezula.eventsourcing.serializer.JsonAsMapSerializer
import com.tomaszezula.eventsourcing.serializer.SerializerRegistry.jsonSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ZoomEvent(
    @SerialName("event") override val name: String,
    @SerialName("event_ts") val timestamp: Long,
    @Serializable(with = JsonAsMapSerializer::class)
    val payload: Map<String, String>,
) : ApiEvent {
    companion object {
        const val OBJECT_KEY = "object"
    }

    override fun toModel(vararg property: DynamicProperty<*>): Event {
        val fieldMap = payload.filterKeys { it != OBJECT_KEY }
        val objectMap = payload[OBJECT_KEY]?.let { jsonSerializer.decodeFromString(JsonAsMapSerializer, it) } ?: emptyMap()
        return Event(
            name = name,
            timestamp = timestamp,
            context = DynamicContext.from(fieldMap + objectMap, *property)
        )
    }
}
