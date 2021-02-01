package bvanseg.kotlincommons.time.api.transformer

import bvanseg.kotlincommons.time.api.KTimeUnit

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
object MillenniumTransformer : KTimeTransformer {
    override fun transform(value: Double, unit: KTimeUnit): Double {
        return when (unit) {
            KTimeUnit.NANOSECOND -> value * 1_000_000.0 * 1000 * 60 * 60 * 24 * 365 * 1000
            KTimeUnit.MICROSECOND -> value * 1_000.0 * 1000 * 60 * 60 * 24 * 365 * 1000
            KTimeUnit.MILLISECOND -> value * 1000.0 * 60 * 60 * 24 * 365 * 1000
            KTimeUnit.SECOND -> value * 60.0 * 60 * 24 * 365 * 1000
            KTimeUnit.MINUTE -> value * 60.0 * 24 * 365 * 1000
            KTimeUnit.HOUR -> value * 24 * 365 * 1000
            KTimeUnit.DAY -> value * 365 * 1000
            KTimeUnit.WEEK -> (value * 365 * 1000) / 7.0
            KTimeUnit.YEAR -> value * 1000
            KTimeUnit.DECADE -> value * 100
            KTimeUnit.CENTURY -> value * 10
            KTimeUnit.MILLENNIUM -> value
            KTimeUnit.UNKNOWN -> KTimeUnit.UNKNOWN_CONSTANT
        }
    }
}