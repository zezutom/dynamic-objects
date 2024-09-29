package com.tomaszezula.eventsourcing.model

import com.tomaszezula.eventsourcing.SdkException

sealed interface Result<out T> {
    companion object {
        val NullResult = Success(null)
    }
}

@JvmInline
value class Success<T>(val value: T) : Result<T>

@JvmInline
value class Failure(val error: SdkException) : Result<Nothing>
