package bvanseg.kotlincommons.time.api.transformer

import bvanseg.kotlincommons.time.api.KTimeUnit

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
object HourTransformer : KTimeTransformer {
    override fun transform(value: Double, unit: KTimeUnit): Double {
        return when (unit) {
            KTimeUnit.NANOSECOND -> value * 1_000_000.0 * 1000 * 60 * 60
            KTimeUnit.MICROSECOND -> value * 1_000.0 * 1000 * 60 * 60
            KTimeUnit.MILLISECOND -> value * 1000.0 * 60 * 60
            KTimeUnit.SECOND -> value * 60.0 * 60
            KTimeUnit.MINUTE -> value * 60.0
            KTimeUnit.HOUR -> value
            KTimeUnit.HALF_DAY -> value / 12.0
            KTimeUnit.DAY -> value / 24.0
            KTimeUnit.WEEK -> value / (24.0 * 7)
            KTimeUnit.YEAR -> value / (24.0 * 365)
            KTimeUnit.DECADE -> value / (24.0 * 365 * 10)
            KTimeUnit.CENTURY -> value / (24.0 * 365 * 100)
            KTimeUnit.MILLENNIUM -> value / (24.0 * 365 * 1000)
            KTimeUnit.UNKNOWN -> KTimeUnit.UNKNOWN_CONSTANT
        }
    }
}