package com.tomaszezula.eventsourcing.handler

import com.tomaszezula.eventsourcing.SdkException
import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass

typealias RawEvent = String

interface EventHandler {
    // Handle the incoming event.
    suspend fun handle(event: RawEvent)

    // When a verification fails or when an error occurs during the event handling the onError block will be called.
    // You can use it to queue up the original event in a DLQ, for example.
    fun onError(block: suspend (event: RawEvent, SdkException) -> Unit)

    // Register a new event type and its corresponding listener.
    fun on(eventType: String, block: EventListener.() -> Unit)

    // Add a custom serializer for a specific type.
    fun<T : Any> addSerializer(kclass: KClass<T>, serializer: KSerializer<T>)
}
