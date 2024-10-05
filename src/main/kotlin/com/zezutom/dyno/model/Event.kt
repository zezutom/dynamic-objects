package com.zezutom.dyno.model

import com.zezutom.dyno.context.DynamicContext
import com.zezutom.dyno.context.DynamicProperty

data class Event(
    val name: String,
    val timestamp: Long,
    val context: DynamicContext,
) {
    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(property: DynamicProperty<T>): T {
        return when (val value = context[property]) {
            is Result<*> -> when (value) {
                is Success<*> -> return value.value as T
                is Failure -> throw value.error
            }

            else -> value
        }
    }
}
