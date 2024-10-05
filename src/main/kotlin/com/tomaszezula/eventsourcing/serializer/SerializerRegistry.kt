package com.tomaszezula.eventsourcing.serializer

import com.tomaszezula.eventsourcing.context.DynamicProperty
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass

object SerializerRegistry {
    val jsonSerializer = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
    private val serializers = mutableMapOf<KClass<*>, KSerializer<*>>()

    fun register(property: DynamicProperty<*>, serializer: KSerializer<*>) {
        serializers[property.type] = serializer
    }

    fun getSerializer(property: DynamicProperty<*>): KSerializer<*>? {
        return serializers[property.type]
    }
}
