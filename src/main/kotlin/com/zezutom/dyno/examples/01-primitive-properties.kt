package com.zezutom.dyno.examples

import com.zezutom.dyno.context.DynamicProperty.Companion.required
import com.zezutom.dyno.handler.DefaultEventHandler.Companion.handler
import com.zezutom.dyno.model.api.ZoomEvent
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant

fun main() = runBlocking {
    val eventHandler = handler(ZoomEvent.serializer()) {
        onError { event, ex ->
            println("Error handling event: $event")
            println(ex.message)
        }

        listener("meeting.ended") {
            val uuid = add { required<String>("uuid") }
            val startTime = add { required<Instant>("start_time") }

            on { event ->
                println("I'm handling a meeting.ended event.")
                println("UUID: ${event[uuid]}")
                println("Start time: ${event[startTime]}")
            }
        }
    }
    eventHandler.handle(meetingEndedEvent)
}
