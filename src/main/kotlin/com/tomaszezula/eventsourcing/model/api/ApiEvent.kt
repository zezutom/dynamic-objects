package com.tomaszezula.eventsourcing.model.api

import com.tomaszezula.eventsourcing.context.DynamicProperty
import com.tomaszezula.eventsourcing.context.EvalMode
import com.tomaszezula.eventsourcing.model.Event

interface ApiEvent {
    val name: String
    fun toModel(mode: EvalMode, vararg property: DynamicProperty<*>): Event
}
