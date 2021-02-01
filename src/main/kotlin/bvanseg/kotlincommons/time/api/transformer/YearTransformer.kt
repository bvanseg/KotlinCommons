package bvanseg.kotlincommons.time.api.transformer

import bvanseg.kotlincommons.time.api.KTimeUnit

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
object YearTransformer : KTimeTransformer {
    override fun transform(value: Double, unit: KTimeUnit): Double {
        return when (unit) {
            KTimeUnit.NANOSECOND -> value * 1_000_000.0 * 1000 * 60 * 60 * 24 * 365
            KTimeUnit.MICROSECOND -> value * 1_000.0 * 1000 * 60 * 60 * 24 * 365
            KTimeUnit.MILLISECOND -> value * 1000.0 * 60 * 60 * 24 * 365
            KTimeUnit.SECOND -> value * 60.0 * 60 * 24 * 365
            KTimeUnit.MINUTE -> value * 60.0 * 24 * 365
            KTimeUnit.HOUR -> value * 24 * 365
            KTimeUnit.HALF_DAY -> value * 365 * 2
            KTimeUnit.DAY -> value * 365
            KTimeUnit.WEEK -> (value * 365) / 7.0
            KTimeUnit.YEAR -> value
            KTimeUnit.DECADE -> value / 10.0
            KTimeUnit.CENTURY -> value / 100.0
            KTimeUnit.MILLENNIUM -> value / 1000.0
            KTimeUnit.UNKNOWN -> KTimeUnit.UNKNOWN_CONSTANT
        }
    }
}