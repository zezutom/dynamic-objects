package com.tomaszezula.eventsourcing.handler

import com.tomaszezula.eventsourcing.SdkException
import com.tomaszezula.eventsourcing.context.EvalMode

typealias RawEvent = String

interface EventHandler {
    // Handle the incoming event.
    suspend fun handle(event: RawEvent)

    // When a verification fails or when an error occurs during the event handling the onError block will be called.
    // You can use it to queue up the original event in a DLQ, for example.
    fun onError(block: suspend (event: RawEvent, SdkException) -> Unit)

    // Register a new event type and its corresponding listener.
    fun listener(eventType: String, mode: EvalMode = EvalMode.Strict, block: EventListener.() -> Unit)
}
