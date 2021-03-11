package bvanseg.kotlincommons.lang.command.transformer.impl.primitive

import bvanseg.kotlincommons.lang.command.exception.TransformerException
import bvanseg.kotlincommons.lang.command.token.buffer.ArgumentTokenBuffer
import bvanseg.kotlincommons.lang.command.token.buffer.PeekingTokenBuffer
import bvanseg.kotlincommons.lang.command.transformer.Transformer
import bvanseg.kotlincommons.math.format
import java.math.BigInteger

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object LongTransformer: Transformer<Long>(Long::class) {
    private val REGEX = Regex("^[+-]?\\d+\$")
    private val MIN = BigInteger.valueOf(Long.MIN_VALUE)
    private val MAX = BigInteger.valueOf(Long.MAX_VALUE)

    override fun matches(buffer: PeekingTokenBuffer): Boolean = buffer.peek()?.value?.matches(REGEX) ?: false
    override fun parse(buffer: ArgumentTokenBuffer): Long {
        val text = buffer.next().value
        val bigInt = BigInteger(text)
        if (bigInt !in MIN..MAX) {
            throw TransformerException("Value '$bigInt' does not fall within the range of (${MIN.format()} to ${MAX.format()})")
        }
        return text.toLong()
    }
}