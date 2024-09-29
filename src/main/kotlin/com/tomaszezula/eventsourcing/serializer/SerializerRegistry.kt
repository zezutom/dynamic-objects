package com.tomaszezula.eventsourcing.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

object SerializerRegistry {
    val jsonSerializer = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
    private val serializers = mutableMapOf<Class<*>, KSerializer<*>>()

    fun <T> register(clazz: Class<T>, serializer: KSerializer<T>) {
        serializers[clazz] = serializer
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getSerializer(clazz: Class<T>): KSerializer<T>? {
        return serializers[clazz] as KSerializer<T>?
    }
}
