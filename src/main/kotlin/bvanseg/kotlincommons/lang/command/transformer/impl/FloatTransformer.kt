package bvanseg.kotlincommons.lang.command.transformer.impl

import bvanseg.kotlincommons.lang.command.transformer.DecimalTransformer

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object FloatTransformer: DecimalTransformer(Float::class) {
    override fun parse(input: String): Float = super.parse(input).toFloat()
}