package com.zezutom.dyno.handler

import com.zezutom.dyno.context.DynamicProperty
import com.zezutom.dyno.context.EvalMode
import com.zezutom.dyno.model.Event

class DefaultEventListener(override val mode: EvalMode) : EventListener {
    companion object {
        fun listener(mode: EvalMode, init: EventListener.() -> Unit): EventListener {
            return DefaultEventListener(mode).apply(init)
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
