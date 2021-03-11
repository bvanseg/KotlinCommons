package bvanseg.kotlincommons.lang.command.transformer.impl.primitive

import bvanseg.kotlincommons.lang.command.token.buffer.ArgumentTokenBuffer
import bvanseg.kotlincommons.lang.command.transformer.DecimalTransformer

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object DoubleTransformer: DecimalTransformer(Double::class) {
    override fun parse(buffer: ArgumentTokenBuffer): Double = super.parse(buffer).toDouble()
}