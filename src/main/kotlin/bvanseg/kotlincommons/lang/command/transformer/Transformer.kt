package bvanseg.kotlincommons.lang.command.transformer

import bvanseg.kotlincommons.lang.command.token.buffer.ArgumentTokenBuffer
import bvanseg.kotlincommons.lang.command.token.buffer.PeekingTokenBuffer
import kotlin.reflect.KClass

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
abstract class Transformer<T: Any>(val type: KClass<out T>) {
    abstract fun matches(buffer: PeekingTokenBuffer): Boolean
    abstract fun parse(buffer: ArgumentTokenBuffer): T
}