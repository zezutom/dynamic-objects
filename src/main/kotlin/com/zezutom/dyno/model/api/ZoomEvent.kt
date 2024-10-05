package com.zezutom.dyno.model.api

import com.zezutom.dyno.context.DynamicContext
import com.zezutom.dyno.context.DynamicProperty
import com.zezutom.dyno.context.EvalMode
import com.zezutom.dyno.model.Event
import com.zezutom.dyno.serializer.JsonAsMapSerializer
import com.zezutom.dyno.serializer.SerializerRegistry.jsonSerializer
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

    override fun toModel(mode: EvalMode, vararg property: DynamicProperty<*>): Event {
        val fieldMap = payload.filterKeys { it != OBJECT_KEY }
        val objectMap = payload[OBJECT_KEY]?.let { jsonSerializer.decodeFromString(JsonAsMapSerializer, it) } ?: emptyMap()
        return Event(
            name = name,
            timestamp = timestamp,
            context = DynamicContext.from(mode, fieldMap + objectMap, *property)
        )
    }
}
