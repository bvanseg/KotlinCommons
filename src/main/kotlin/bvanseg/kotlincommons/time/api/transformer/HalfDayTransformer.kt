package bvanseg.kotlincommons.time.api.transformer

import bvanseg.kotlincommons.time.api.KTimeUnit

/**
 * @author Boston Vanseghi
 * @since 2.7.1
 */
object HalfDayTransformer : KTimeTransformer {
    override fun transform(value: Double, unit: KTimeUnit): Double {
        return when (unit) {
            KTimeUnit.NEVER -> KTimeUnit.NEVER_CONSTANT
            KTimeUnit.NANOSECOND -> value * 1_000_000.0 * 1000 * 60 * 60 * 12
            KTimeUnit.MICROSECOND -> value * 1_000.0 * 1000 * 60 * 60 * 12
            KTimeUnit.MILLISECOND -> value * 1000.0 * 60 * 60 * 12
            KTimeUnit.SECOND -> value * 60.0 * 60 * 12
            KTimeUnit.MINUTE -> value * 60.0 * 12
            KTimeUnit.HOUR -> value * 12
            KTimeUnit.HALF_DAY -> value
            KTimeUnit.DAY -> value / 2
            KTimeUnit.WEEK -> value / (7.0 * 2)
            KTimeUnit.YEAR -> value / (365.0 * 2)
            KTimeUnit.DECADE -> value / (365.0 * 10 * 2)
            KTimeUnit.CENTURY -> value / (365.0 * 100 * 2)
            KTimeUnit.MILLENNIUM -> value / (365.0 * 1000 * 2)
            KTimeUnit.FOREVER -> KTimeUnit.FOREVER_CONSTANT
        }
    }
}