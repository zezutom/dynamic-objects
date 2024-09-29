package com.tomaszezula.eventsourcing.context

import com.tomaszezula.eventsourcing.SdkException
import com.tomaszezula.eventsourcing.attempt
import com.tomaszezula.eventsourcing.model.Failure
import com.tomaszezula.eventsourcing.model.Result
import com.tomaszezula.eventsourcing.model.Result.Companion.NullResult
import kotlin.reflect.KClass

typealias PropertyValidator<T> = (T?) -> Boolean

interface DynamicProperty<T> {
    val name: String
    val type: KClass<*>
    fun cast(value: Any?): Result<T>
    fun default(): Result<T>

    companion object {
        inline fun <reified T> fromDefaultSupplier(
            name: String,
            crossinline default: () -> Result<T>,
            crossinline validator: PropertyValidator<T> = { true },
        ) =
            object : DynamicProperty<T> {
                override val name = name
                override val type = T::class
                override fun cast(value: Any?): Result<T> = attempt {
                    val castValue = value as? T
                        ?: throw SdkException("Error casting property $name to type ${type.qualifiedName} with value $value")
                    if (validator(castValue)) castValue else throw SdkException("Invalid value for property $name: $value")
                }

                override fun default(): Result<T> = default()
            }

        inline fun <reified T> required(name: String) = fromDefaultSupplier<T>(name, {
            Failure(SdkException("Property $name is required"))
        })

        inline fun <reified T> required(name: String, default: Result<T>) = fromDefaultSupplier(name, { default })
        inline fun <reified T> required(name: String, default: Result<T>, crossinline validator: PropertyValidator<T>) =
            fromDefaultSupplier(name, { default }, validator)

        inline fun <reified T> nullable(name: String) = fromDefaultSupplier<T?>(name, { NullResult })
        inline fun <reified T> nullable(name: String, crossinline validator: PropertyValidator<T?>) =
            fromDefaultSupplier<T?>(name, { NullResult }, validator)
    }
}
