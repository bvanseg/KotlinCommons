package bvanseg.kotlincommons.time.api.transformer

import bvanseg.kotlincommons.time.api.KTimeUnit

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
object DayTransformer : KTimeTransformer {
    override fun transform(value: Double, unit: KTimeUnit): Double {
        return when (unit) {
            KTimeUnit.NEVER -> KTimeUnit.NEVER_CONSTANT
            KTimeUnit.NANOSECOND -> value * 1_000_000.0 * 1000 * 60 * 60 * 24
            KTimeUnit.MICROSECOND -> value * 1_000.0 * 1000 * 60 * 60 * 24
            KTimeUnit.MILLISECOND -> value * 1000.0 * 60 * 60 * 24
            KTimeUnit.SECOND -> value * 60.0 * 60 * 24
            KTimeUnit.MINUTE -> value * 60.0 * 24
            KTimeUnit.HOUR -> value * 24
            KTimeUnit.HALF_DAY -> value * 2
            KTimeUnit.DAY -> value
            KTimeUnit.WEEK -> value / 7.0
            KTimeUnit.YEAR -> value / 365.0
            KTimeUnit.DECADE -> value / (365.0 * 10)
            KTimeUnit.CENTURY -> value / (365.0 * 100)
            KTimeUnit.MILLENNIUM -> value / (365.0 * 1000)
            KTimeUnit.FOREVER -> KTimeUnit.FOREVER_CONSTANT
        }
    }
}