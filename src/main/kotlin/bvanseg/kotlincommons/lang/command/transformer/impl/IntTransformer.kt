package bvanseg.kotlincommons.lang.command.transformer.impl

import bvanseg.kotlincommons.lang.command.token.buffer.ArgumentTokenBuffer
import bvanseg.kotlincommons.lang.command.transformer.Transformer

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object IntTransformer: Transformer<Int>(Int::class) {
    private val REGEX = Regex("^[+-]?\\d+\$")
    override fun matches(buffer: ArgumentTokenBuffer): Boolean = buffer.peek()?.value?.matches(REGEX) ?: false
    override fun parse(buffer: ArgumentTokenBuffer): Int = buffer.next().value.toInt()
}