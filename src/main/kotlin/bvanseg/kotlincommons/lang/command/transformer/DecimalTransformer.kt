package bvanseg.kotlincommons.lang.command.transformer

import bvanseg.kotlincommons.lang.command.exception.TransformerException
import bvanseg.kotlincommons.lang.command.token.buffer.ArgumentTokenBuffer
import bvanseg.kotlincommons.lang.command.token.buffer.PeekingTokenBuffer
import java.math.BigDecimal
import kotlin.reflect.KClass

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
abstract class DecimalTransformer internal constructor(type: KClass<out Number>): Transformer<Number>(type) {
    private val REGEX = Regex("^[+-]?([0-9]*[.])?[0-9]+$")
    private val MIN = if (type == Float::class) BigDecimal.valueOf(Float.MIN_VALUE.toDouble()) else BigDecimal.valueOf(Double.MIN_VALUE)
    private val MAX = if (type == Float::class) BigDecimal.valueOf(Float.MAX_VALUE.toDouble()) else BigDecimal.valueOf(Double.MAX_VALUE)

    override fun matches(buffer: PeekingTokenBuffer): Boolean {
        val token = buffer.peek() ?: return false
        val input = token.value
        return input.matches(REGEX) || input.equals("pi", true) || input.equals("e", true)
    }

    override fun parse(buffer: ArgumentTokenBuffer): Number {
        val text = buffer.next().value.replace("pi", Math.PI.toString(), true)
            .replace("e", Math.E.toString(), true)
        val bigDeci = BigDecimal(text)
        if (bigDeci !in MIN..MAX) {
            throw TransformerException("Value '$bigDeci' does not fall within the range of ($MIN to $MAX)")
        }
        return if (type == Float::class) text.toFloat() else text.toDouble()
    }
}