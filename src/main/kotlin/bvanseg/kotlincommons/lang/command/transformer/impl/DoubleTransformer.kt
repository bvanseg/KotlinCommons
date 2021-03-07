package bvanseg.kotlincommons.lang.command.transformer.impl

import bvanseg.kotlincommons.lang.command.transformer.DecimalTransformer

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object DoubleTransformer: DecimalTransformer(Double::class) {
    override fun parse(input: String): Double = super.parse(input).toDouble()
}