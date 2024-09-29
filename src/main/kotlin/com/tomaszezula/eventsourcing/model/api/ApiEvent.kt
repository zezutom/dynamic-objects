package com.tomaszezula.eventsourcing.model.api

import com.tomaszezula.eventsourcing.context.DynamicProperty
import com.tomaszezula.eventsourcing.model.Event

interface ApiEvent {
    val name: String
    fun toModel(vararg property: DynamicProperty<*>): Event
}
