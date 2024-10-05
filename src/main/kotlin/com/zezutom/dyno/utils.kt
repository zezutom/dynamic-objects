package com.zezutom.dyno

import kotlin.coroutines.cancellation.CancellationException

fun<T> attempt(block: () -> T): com.zezutom.dyno.model.Result<T> {
    return try {
        com.zezutom.dyno.model.Success(block())
    } catch (e: Exception) {
        when (e) {
            is SdkException -> com.zezutom.dyno.model.Failure(e)
            else -> com.zezutom.dyno.model.Failure(SdkException(e))
        }
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
