package bvanseg.kotlincommons.time.api.transformer

import bvanseg.kotlincommons.time.api.KTimeUnit

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
object DecadeTransformer : KTimeTransformer {
    override fun transform(value: Double, unit: KTimeUnit): Double {
        return when (unit) {
            KTimeUnit.NEVER -> KTimeUnit.NEVER_CONSTANT
            KTimeUnit.NANOSECOND -> value * 1_000_000.0 * 1000 * 60 * 60 * 24 * 365 * 10
            KTimeUnit.MICROSECOND -> value * 1_000.0 * 1000 * 60 * 60 * 24 * 365 * 10
            KTimeUnit.MILLISECOND -> value * 1000.0 * 60 * 60 * 24 * 365 * 10
            KTimeUnit.SECOND -> value * 60.0 * 60 * 24 * 365 * 10
            KTimeUnit.MINUTE -> value * 60.0 * 24 * 365 * 10
            KTimeUnit.HOUR -> value * 24 * 365 * 10
            KTimeUnit.HALF_DAY -> value * 2 * 365 * 10
            KTimeUnit.DAY -> value * 365 * 10
            KTimeUnit.WEEK -> (value * 365 * 10) / 7.0
            KTimeUnit.YEAR -> value * 10
            KTimeUnit.DECADE -> value
            KTimeUnit.CENTURY -> value / 10.0
            KTimeUnit.MILLENNIUM -> value / 100.0
            KTimeUnit.FOREVER -> KTimeUnit.FOREVER_CONSTANT
        }
    }
}