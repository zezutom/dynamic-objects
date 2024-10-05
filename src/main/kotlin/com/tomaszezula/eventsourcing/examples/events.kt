package com.tomaszezula.eventsourcing.examples

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

val recordingCompletedEvent =
    """
        {
          "event": "recording.completed",
          "event_ts": 1626230691572,
          "payload": {
            "account_id": "your_account_id",
            "object": {
              "uuid": "meeting_uuid",
              "id": "meeting_id",
              "account_id": "account_associated_with_meeting",
              "host_id": "host_user_id",
              "topic": "Meeting Topic",
              "type": 2,
              "start_time": "2022-08-03T12:00:00Z",
              "timezone": "America/Los_Angeles",
              "duration": 60,
              "total_size": 123456,
              "recording_count": 2,
              "share_url": "https://zoom.us/recording/share/xyz",
              "recording_files": [
                {
                  "id": "file_id1",
                  "meeting_id": "meeting_id_associated_with_file",
                  "recording_start": "2022-08-03T12:00:00Z",
                  "recording_end": "2022-08-03T13:00:00Z",
                  "file_type": "MP4",
                  "file_size": 123456,
                  "play_url": "https://zoom.us/recording/play/abc",
                  "download_url": "https://zoom.us/recording/download/abc",
                  "status": "completed",
                  "recording_type": "shared_screen_with_speaker_view"
                },
                {
                  "id": "file_id2",
                  "meeting_id": "meeting_id_associated_with_file",
                  "recording_start": "2022-08-03T12:00:00Z",
                  "recording_end": "2022-08-03T13:00:00Z",
                  "file_type": "M4A",
                  "file_size": 456789,
                  "play_url": "https://zoom.us/recording/play/def",
                  "download_url": "https://zoom.us/recording/download/def",
                  "status": "completed",
                  "recording_type": "audio_only"
                }
              ]
            }
          }
        }
    """.trimIndent()
