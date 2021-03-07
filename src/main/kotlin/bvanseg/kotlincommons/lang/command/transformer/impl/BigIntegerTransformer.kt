package bvanseg.kotlincommons.lang.command.transformer.impl

import bvanseg.kotlincommons.lang.command.transformer.Transformer
import java.math.BigInteger

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object BigIntegerTransformer: Transformer<BigInteger>(BigInteger::class) {
    private val REGEX = Regex("^[+-]?\\d+\$")
    override fun matches(input: String): Boolean = input.matches(REGEX)
    override fun parse(input: String): BigInteger = BigInteger(input)
}