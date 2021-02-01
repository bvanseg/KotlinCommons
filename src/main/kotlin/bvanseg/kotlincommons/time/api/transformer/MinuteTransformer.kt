package bvanseg.kotlincommons.time.api.transformer

import bvanseg.kotlincommons.time.api.KTimeUnit

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
object MinuteTransformer : KTimeTransformer {
    override fun transform(value: Double, unit: KTimeUnit): Double {
        return when (unit) {
            KTimeUnit.NANOSECOND -> value * 1_000_000.0 * 1000 * 60
            KTimeUnit.MICROSECOND -> value * 1_000.0 * 1000 * 60
            KTimeUnit.MILLISECOND -> value * 1000.0 * 60
            KTimeUnit.SECOND -> value * 60.0
            KTimeUnit.MINUTE -> value
            KTimeUnit.HOUR -> value / 60.0
            KTimeUnit.DAY -> value / (60.0 * 24)
            KTimeUnit.WEEK -> value / (60.0 * 24 * 7)
            KTimeUnit.YEAR -> value / (60.0 * 24 * 365)
            KTimeUnit.DECADE -> value / (60.0 * 24 * 365 * 10)
            KTimeUnit.CENTURY -> value / (60.0 * 24 * 365 * 100)
            KTimeUnit.MILLENNIUM -> value / (60.0 * 24 * 365 * 1000)
            KTimeUnit.UNKNOWN -> KTimeUnit.UNKNOWN_CONSTANT
        }
    }
}