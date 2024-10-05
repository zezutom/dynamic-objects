package com.zezutom.dyno.model.api

import com.zezutom.dyno.context.DynamicProperty
import com.zezutom.dyno.context.EvalMode
import com.zezutom.dyno.model.Event

interface ApiEvent {
    val name: String
    fun toModel(mode: EvalMode, vararg property: DynamicProperty<*>): Event
}
