package bvanseg.kotlincommons.lang.command.transformer.impl.primitive

import bvanseg.kotlincommons.lang.command.token.buffer.ArgumentTokenBuffer
import bvanseg.kotlincommons.lang.command.token.buffer.PeekingTokenBuffer
import bvanseg.kotlincommons.lang.command.transformer.Transformer

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object CharTransformer: Transformer<Char>(Char::class) {
    override fun matches(buffer: PeekingTokenBuffer): Boolean {
        val text = buffer.peek()?.value ?: return false
        return text.length == 1
    }
    override fun parse(buffer: ArgumentTokenBuffer): Char = buffer.next().value[0]
}