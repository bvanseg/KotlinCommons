package bvanseg.kotlincommons.time.api.transformer

import bvanseg.kotlincommons.time.api.KTimeUnit

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
interface KTimeTransformer {
    fun transform(value: Double, unit: KTimeUnit): Double
}