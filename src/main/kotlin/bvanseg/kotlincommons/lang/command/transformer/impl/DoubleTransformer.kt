package bvanseg.kotlincommons.lang.command.transformer.impl

import bvanseg.kotlincommons.lang.command.transformer.Transformer

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object DoubleTransformer: Transformer<Double>(Double::class) {
    private val REGEX = Regex("^[+-]?([0-9]*[.])?[0-9]+$")
    override fun matches(input: String): Boolean = input.matches(REGEX)
    override fun parse(input: String): Double = input.toDouble()
}