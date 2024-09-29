package com.tomaszezula.eventsourcing

import com.tomaszezula.eventsourcing.model.Failure
import com.tomaszezula.eventsourcing.model.Result
import com.tomaszezula.eventsourcing.model.Success
import kotlin.coroutines.cancellation.CancellationException

fun<T> attempt(block: () -> T): Result<T> {
    return try {
        Success(block())
    } catch (e: SdkException) {
        Failure(e)
    }
}

suspend fun <T> tryCall(onError: suspend (SdkException) -> T, block: suspend () -> T): T {
    return try {
        block()
    } catch (e: CancellationException) {
        // Respect cancellation
        throw e
    } catch (e: Exception) {
        when (e) {
            is SdkException -> onError(e)
            else -> onError(SdkException(e))
        }
    }
}
