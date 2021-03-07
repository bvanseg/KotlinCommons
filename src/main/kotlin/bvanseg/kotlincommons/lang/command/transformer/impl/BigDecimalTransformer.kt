package bvanseg.kotlincommons.lang.command.transformer.impl

import bvanseg.kotlincommons.lang.command.transformer.Transformer
import java.math.BigDecimal

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object BigDecimalTransformer: Transformer<BigDecimal>(BigDecimal::class) {
    private val REGEX = Regex("^[+-]?([0-9]*[.])?[0-9]+$")
    override fun matches(input: String): Boolean = input.matches(REGEX)
    override fun parse(input: String): BigDecimal = BigDecimal(input)
}