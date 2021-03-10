package bvanseg.kotlincommons.lang.command.transformer.impl

import bvanseg.kotlincommons.lang.command.token.buffer.ArgumentTokenBuffer
import bvanseg.kotlincommons.lang.command.token.buffer.PeekingTokenBuffer
import bvanseg.kotlincommons.lang.command.transformer.Transformer

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object BooleanTransformer: Transformer<Boolean>(Boolean::class) {
    private val REGEX = Regex("^true|false$", RegexOption.IGNORE_CASE)
    override fun matches(buffer: PeekingTokenBuffer): Boolean = buffer.peek()?.value?.matches(REGEX) ?: false
    override fun parse(buffer: ArgumentTokenBuffer): Boolean = buffer.next().value.toBoolean()
}