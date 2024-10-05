package com.tomaszezula.eventsourcing.examples

import com.tomaszezula.eventsourcing.context.DynamicProperty.Companion.nullable
import com.tomaszezula.eventsourcing.context.DynamicProperty.Companion.required
import com.tomaszezula.eventsourcing.context.EvalMode
import com.tomaszezula.eventsourcing.handler.DefaultEventHandler.Companion.handler
import com.tomaszezula.eventsourcing.model.api.ZoomEvent
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val eventHandler = handler(ZoomEvent.serializer()) {
        onError { event, ex ->
            println("Error handling event: $event")
            println(ex.message)
        }

        listener("meeting.ended", mode = EvalMode.Strict) {
            val uuid = add { required<String>("uuid") }
            val okNonsense = add { nullable<Int>("nonsense") }
            val badNonsense = add { required<Int>("bad_nonsense") }

            on { event ->
                println("I'm handling a meeting.ended event.")
                println("UUID: ${event[uuid]}")
            }
        }
    }
    eventHandler.handle(meetingEndedEvent)
}
