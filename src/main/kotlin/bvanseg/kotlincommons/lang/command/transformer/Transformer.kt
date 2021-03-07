package bvanseg.kotlincommons.lang.command.transformer

import kotlin.reflect.KClass

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
abstract class Transformer<T: Any>(val type: KClass<out T>) {
    abstract fun matches(input: String): Boolean
    abstract fun parse(input: String): T
}