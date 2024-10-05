package com.tomaszezula.eventsourcing.examples

import com.tomaszezula.eventsourcing.context.DynamicProperty.Companion.required
import com.tomaszezula.eventsourcing.context.withSerializer
import com.tomaszezula.eventsourcing.handler.DefaultEventHandler.Companion.handler
import com.tomaszezula.eventsourcing.model.api.ZoomEvent
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer

@Serializable
data class Recording(
    @SerialName("meeting_id") val meetingId: String,
    @SerialName("file_type") val fileType: String,
    @SerialName("download_url") val downloadUrl: String,
)

fun main() = runBlocking {
    val eventHandler = handler(ZoomEvent.serializer()) {
        onError { event, ex ->
            println("Error handling event: $event")
            println(ex.message)
        }

        listener("recording.completed") {
            val recordingFiles = add { required<List<Recording>>("recording_files") }
                .withSerializer(ListSerializer(Recording.serializer()))

            on { event ->
                println("I'm handling a recording.completed event.")
                event[recordingFiles].forEach { recording ->
                    println("Meeting ID: ${recording.meetingId}")
                    println("File type: ${recording.fileType}")
                    println("Download URL: ${recording.downloadUrl}")
                }
            }
        }
    }
    eventHandler.handle(recordingCompletedEvent)
}
