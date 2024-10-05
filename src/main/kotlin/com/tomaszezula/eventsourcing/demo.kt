package com.tomaszezula.eventsourcing

import com.tomaszezula.eventsourcing.context.DynamicProperty.Companion.required
import com.tomaszezula.eventsourcing.context.EvalMode
import com.tomaszezula.eventsourcing.handler.DefaultEventHandler.Companion.handler
import com.tomaszezula.eventsourcing.model.api.ZoomEvent
import com.tomaszezula.eventsourcing.serializer.JavaInstantSerializer
import kotlinx.coroutines.runBlocking
import java.time.Instant

fun main() = runBlocking {
    // Define a new event handler.
    val zoomEventHandler = handler(ZoomEvent.serializer()) {
        onError { event, sdkException ->
            println("Error handling event: $event")
            println("Error: $sdkException")
        }

        addSerializer(Instant::class, JavaInstantSerializer)

        on("meeting.ended", mode = EvalMode.Strict) {
            val uuid = add { required<String>("uuid") }
            val startTime = add { required<Instant>("start_time") }

            // This is going to fail fast with Strict mode
//            val nonExistentInstant = add { required<Instant>("made_up_time") }
//            val nonExistentString = add { nullable<String>("i_do_not_exist") }
            on { event ->
                println("I'm handling a meeting.ended event.")
                println("UUID: ${event[uuid]}")
                println("Start time: ${event[startTime]}")
//                println("Made-up time: ${event[nonExistentInstant]}")
//                println("Made-up string: ${event[nonExistentString]}")
            }
        }
    }

    // Handle a new event.
    zoomEventHandler.handle(meetingEndedEvent)
}

val meetingEndedEvent =
    """
        {
          "event": "meeting.ended",
          "event_ts": 1626230691572,
          "payload": {
            "account_id": "AAAAAABBBB",
            "operator": "admin@example.com",
            "operator_id": "z8yCxjabcdEFGHfp8uQ",
            "operation": "single",
            "object": {
              "id": "1234567890",
              "uuid": "4444AAAiAAAAAiAiAiiAii==",
              "host_id": "x1yCzABCDEfg23HiJKl4mN",
              "topic": "My Meeting",
              "type": 3,
              "start_time": "2021-07-13T21:44:51Z",
              "timezone": "America/Los_Angeles",
              "duration": 60,
              "end_time": "2021-07-13T23:00:51Z"
            }
          }
        }        
    """.trimIndent()
