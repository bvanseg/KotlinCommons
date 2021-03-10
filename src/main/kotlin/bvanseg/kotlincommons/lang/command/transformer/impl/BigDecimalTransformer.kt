package bvanseg.kotlincommons.lang.command.transformer.impl

import bvanseg.kotlincommons.lang.command.token.buffer.ArgumentTokenBuffer
import bvanseg.kotlincommons.lang.command.token.buffer.PeekingTokenBuffer
import bvanseg.kotlincommons.lang.command.transformer.Transformer
import java.math.BigDecimal

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object BigDecimalTransformer: Transformer<BigDecimal>(BigDecimal::class) {
    private val REGEX = Regex("^[+-]?([0-9]*[.])?[0-9]+$")
    override fun matches(buffer: PeekingTokenBuffer): Boolean = buffer.peek()?.value?.matches(REGEX) ?: false
    override fun parse(buffer: ArgumentTokenBuffer): BigDecimal = BigDecimal(buffer.next().value)
}