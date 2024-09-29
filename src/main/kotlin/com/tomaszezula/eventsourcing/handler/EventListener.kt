package com.tomaszezula.eventsourcing.handler

import com.tomaszezula.eventsourcing.context.DynamicProperty
import com.tomaszezula.eventsourcing.model.Event

interface EventListener {
    suspend fun on(event: Event)

    // This is where you define the block that will be called when the fully initialized event is received.
    fun on(block: suspend (Event) -> Unit)

    // This is where you add a new property to the event context.
    fun <T> add(block: () -> DynamicProperty<T>): DynamicProperty<T>

    fun properties(): Array<DynamicProperty<*>>
}
