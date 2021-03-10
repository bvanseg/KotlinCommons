package bvanseg.kotlincommons.lang.command.transformer.impl

import bvanseg.kotlincommons.lang.command.token.buffer.ArgumentTokenBuffer
import bvanseg.kotlincommons.lang.command.token.buffer.PeekingTokenBuffer
import bvanseg.kotlincommons.lang.command.transformer.Transformer

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object ArgumentTokenBufferTransformer: Transformer<ArgumentTokenBuffer>(ArgumentTokenBuffer::class) {
    override fun matches(buffer: PeekingTokenBuffer): Boolean = true
    override fun parse(buffer: ArgumentTokenBuffer): ArgumentTokenBuffer = ArgumentTokenBuffer(buffer.all())
}