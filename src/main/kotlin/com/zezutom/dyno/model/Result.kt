package com.zezutom.dyno.model

import com.zezutom.dyno.SdkException

sealed interface Result<out T> {
    companion object {
        val NullResult = Success(null)
    }
}

@JvmInline
value class Success<T>(val value: T) : Result<T>

@JvmInline
value class Failure(val error: SdkException) : Result<Nothing>
