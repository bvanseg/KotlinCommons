package bvanseg.kotlincommons.time.api.transformer

import bvanseg.kotlincommons.time.api.KTimeUnit

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
object MillisecondTransformer : KTimeTransformer {
    override fun transform(value: Double, unit: KTimeUnit): Double {
        return when (unit) {
            KTimeUnit.NEVER -> KTimeUnit.NEVER_CONSTANT
            KTimeUnit.NANOSECOND -> value * 1_000_000.0
            KTimeUnit.MICROSECOND -> value * 1_000.0
            KTimeUnit.MILLISECOND -> value
            KTimeUnit.SECOND -> value / 1000.0
            KTimeUnit.MINUTE -> value / (1000.0 * 60)
            KTimeUnit.HOUR -> value / (1000.0 * 60 * 60)
            KTimeUnit.HALF_DAY -> value / (1000.0 * 60 * 60 * 24 * 2)
            KTimeUnit.DAY -> value / (1000.0 * 60 * 60 * 24)
            KTimeUnit.WEEK -> value / (1000.0 * 60 * 60 * 24 * 7)
            KTimeUnit.YEAR -> value / (1000.0 * 60 * 60 * 24 * 365)
            KTimeUnit.DECADE -> value / (1000.0 * 60 * 60 * 24 * 365 * 10)
            KTimeUnit.CENTURY -> value / (1000.0 * 60 * 60 * 24 * 365 * 100)
            KTimeUnit.MILLENNIUM -> value / (1000.0 * 60 * 60 * 24 * 365 * 1000)
            KTimeUnit.FOREVER -> KTimeUnit.FOREVER_CONSTANT
        }
    }
}