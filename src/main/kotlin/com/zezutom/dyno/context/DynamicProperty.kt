package com.zezutom.dyno.context

import com.zezutom.dyno.SdkException
import com.zezutom.dyno.attempt
import com.zezutom.dyno.model.Result.Companion.NullResult
import com.zezutom.dyno.serializer.SerializerRegistry
import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass

typealias PropertyValidator<T> = (T?) -> Boolean

interface DynamicProperty<T> {
    val name: String
    val type: KClass<*>
    val required: Boolean get() = true
    fun cast(value: Any?): com.zezutom.dyno.model.Result<T>
    fun default(): com.zezutom.dyno.model.Result<T>

    companion object {
        inline fun <reified T> fromDefaultSupplier(
            name: String,
            required: Boolean,
            crossinline default: () -> com.zezutom.dyno.model.Result<T>,
            crossinline validator: PropertyValidator<T> = { true },
        ) =
            object : DynamicProperty<T> {
                override val name = name
                override val required = required
                override val type = T::class
                override fun cast(value: Any?): com.zezutom.dyno.model.Result<T> = attempt {
                    val castValue = value as? T
                        ?: throw SdkException("Error casting property $name to type ${type.qualifiedName} with value $value")
                    if (validator(castValue)) castValue else throw SdkException("Invalid value for property $name: $value")
                }

                override fun default(): com.zezutom.dyno.model.Result<T> = default()
            }

        inline fun <reified T> required(name: String) = fromDefaultSupplier<T>(name, true, {
            com.zezutom.dyno.model.Failure(SdkException("Property $name is required"))
        })

        inline fun <reified T> required(name: String, default: com.zezutom.dyno.model.Result<T>) = fromDefaultSupplier(name, true, { default })
        inline fun <reified T> required(name: String, default: com.zezutom.dyno.model.Result<T>, crossinline validator: PropertyValidator<T>) =
            fromDefaultSupplier(name, true, { default }, validator)

        inline fun <reified T> nullable(name: String) = fromDefaultSupplier<T?>(name, false, { NullResult })
        inline fun <reified T> nullable(name: String, crossinline validator: PropertyValidator<T?>) =
            fromDefaultSupplier<T?>(name, false, { NullResult }, validator)
    }
}

fun<T> DynamicProperty<T>.withSerializer(serialize: KSerializer<T>): DynamicProperty<T> {
    SerializerRegistry.register(this, serialize)
    return this
}
