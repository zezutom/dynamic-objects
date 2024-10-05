package com.zezutom.dyno.context

import com.zezutom.dyno.SdkException
import com.zezutom.dyno.attempt
import com.zezutom.dyno.context.EvalMode.Strict
import com.zezutom.dyno.serializer.SerializerRegistry.getSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive

typealias LazyValue = () -> com.zezutom.dyno.model.Result<*>

class DynamicContext {
    companion object {
        private val jsonSerializer = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

        /**
         * This is called with each event to validate and cast the properties.
         * The resulting context is then passed to the event's context.
         */
        fun from(mode: EvalMode, source: Map<String, Any?>, vararg property: DynamicProperty<*>): DynamicContext {
            val context = DynamicContext()
            source.forEach { (key, value) ->
                property.firstOrNull { it.name == key }?.let { p ->
                    val result = cast(p, value.toString())
                    if (mode == Strict && result is com.zezutom.dyno.model.Failure) {
                        throw result.error
                    }
                    context.set(p) { result }
                }
            }
            return when (mode) {
                Strict -> {
                    val missingProperties = property.toSet() - context.properties.keys.toSet()
                    missingProperties.filter { it.required }.let { missing ->
                        if (missing.isNotEmpty()) {
                            throw SdkException("Missing required properties: ${missing.joinToString { it.name }}")
                        }
                    }
                    context
                }
                else -> context
            }
        }

        @OptIn(ExperimentalSerializationApi::class)
        private fun cast(property: DynamicProperty<*>, value: String): com.zezutom.dyno.model.Result<Any?> = attempt {
            val propertyValue: Any? = when (property.type) {
                String::class -> value
                Byte::class -> value.toByte()
                Short::class -> value.toShort()
                Int::class -> value.toInt()
                Long::class -> value.toLong()
                Float::class -> value.toFloat()
                Double::class -> value.toDouble()
                Boolean::class -> value.toBoolean()
                Instant::class -> Instant.parse(value)
                else -> {
                    getSerializer(property)?.let {
                        when (it.descriptor.kind) {
                            is PrimitiveKind -> jsonSerializer.decodeFromJsonElement(it, JsonPrimitive(value))
                            else -> jsonSerializer.decodeFromString(it, value)
                        }
                    }
                }
            }
            propertyValue?.let { com.zezutom.dyno.model.Success(it) } ?: property.cast(value)
        }
    }

    private val properties = mutableMapOf<DynamicProperty<*>, LazyValue>()

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(property: DynamicProperty<T>): T {
        val result = if (properties.contains(property)) {
            properties[property]?.invoke()
        } else {
            property.default()
        }
        when (result) {
            is com.zezutom.dyno.model.Success -> return result.value as T
            else -> throw SdkException("Error casting property ${property.name} to type ${property.type.qualifiedName}")
        }
    }

    fun <T> set(property: DynamicProperty<T>, value: LazyValue) {
        properties[property] = value
    }
}


