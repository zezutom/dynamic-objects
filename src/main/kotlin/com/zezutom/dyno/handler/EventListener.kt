package com.zezutom.dyno.handler

import com.zezutom.dyno.context.DynamicProperty
import com.zezutom.dyno.context.EvalMode
import com.zezutom.dyno.model.Event

interface EventListener {
    val mode: EvalMode

    suspend fun on(event: Event)

    // This is where you define the block that will be called when the fully initialized event is received.
    fun on(block: suspend (Event) -> Unit)

    // This is where you add a new property to the event context.
    fun <T> add(block: () -> DynamicProperty<T>): DynamicProperty<T>

    fun properties(): Array<DynamicProperty<*>>
}
