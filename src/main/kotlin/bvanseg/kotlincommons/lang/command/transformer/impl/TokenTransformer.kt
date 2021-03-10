package bvanseg.kotlincommons.lang.command.transformer.impl

import bvanseg.kotlincommons.lang.command.token.Token
import bvanseg.kotlincommons.lang.command.token.buffer.ArgumentTokenBuffer
import bvanseg.kotlincommons.lang.command.token.buffer.PeekingTokenBuffer
import bvanseg.kotlincommons.lang.command.transformer.Transformer

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object TokenTransformer: Transformer<Token>(Token::class) {
    override fun matches(buffer: PeekingTokenBuffer): Boolean = buffer.peek() != null
    override fun parse(buffer: ArgumentTokenBuffer): Token = buffer.next()
}