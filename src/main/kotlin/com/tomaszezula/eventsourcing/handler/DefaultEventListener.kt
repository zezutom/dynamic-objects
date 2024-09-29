package com.tomaszezula.eventsourcing.handler

import com.tomaszezula.eventsourcing.context.DynamicProperty
import com.tomaszezula.eventsourcing.model.Event

class DefaultEventListener : EventListener {
    companion object {
        fun listener(init: EventListener.() -> Unit): EventListener {
            return DefaultEventListener().apply(init)
        }
    }

    private val properties: MutableList<DynamicProperty<*>> = mutableListOf()

    private lateinit var handler: suspend (Event) -> Unit

    override suspend fun on(event: Event) {
        handler(event)
    }

    override fun on(block: suspend (Event) -> Unit) {
        handler = block
    }

    override fun <T> add(block: () -> DynamicProperty<T>): DynamicProperty<T> {
        val property = block()
        properties.add(property)
        return property
    }

    override fun properties(): Array<DynamicProperty<*>> {
        return properties.toTypedArray()
    }
}
