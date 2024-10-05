package com.tomaszezula.eventsourcing.handler

import com.tomaszezula.eventsourcing.SdkException
import com.tomaszezula.eventsourcing.context.EvalMode
import com.tomaszezula.eventsourcing.handler.DefaultEventListener.Companion.listener
import com.tomaszezula.eventsourcing.model.api.ApiEvent
import com.tomaszezula.eventsourcing.serializer.SerializerRegistry.jsonSerializer
import com.tomaszezula.eventsourcing.tryCall
import kotlinx.serialization.KSerializer

class DefaultEventHandler<T : ApiEvent>(private val serializer: KSerializer<T>) : EventHandler {
    companion object {
        fun <T : ApiEvent> handler(serializer: KSerializer<T>, init: EventHandler.() -> Unit): EventHandler {
            return DefaultEventHandler(serializer).apply(init)
        }
    }

    private val listeners = mutableMapOf<String, EventListener>()

    private var errorHandler: suspend (RawEvent, SdkException) -> Unit = { _, _ -> }

    override suspend fun handle(event: RawEvent) {
        tryCall({ errorHandler(event, it) }) {
            val apiEvent = jsonSerializer.decodeFromString(serializer, event)
            listeners[apiEvent.name]?.let { listener ->
                listener.on(apiEvent.toModel(listener.mode, *listener.properties()))
            }
        }
    }

    override fun onError(block: suspend (event: RawEvent, SdkException) -> Unit) {
        errorHandler = block
    }

    override fun listener(eventType: String, mode: EvalMode, block: EventListener.() -> Unit) {
        listeners[eventType] = listener(mode, block)
    }
}
