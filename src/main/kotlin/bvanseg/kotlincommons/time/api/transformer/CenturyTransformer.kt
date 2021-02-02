package bvanseg.kotlincommons.time.api.transformer

import bvanseg.kotlincommons.time.api.KhronoUnit

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
object CenturyTransformer : KTimeTransformer {
    override fun transform(value: Double, unit: KhronoUnit): Double {
        return when (unit) {
            KhronoUnit.NEVER -> KhronoUnit.NEVER_CONSTANT
            KhronoUnit.NANOSECOND -> value * 1_000_000.0 * 1000 * 60 * 60 * 24 * 365 * 100
            KhronoUnit.MICROSECOND -> value * 1_000.0 * 1000 * 60 * 60 * 24 * 365 * 100
            KhronoUnit.MILLISECOND -> value * 1000.0 * 60 * 60 * 24 * 365 * 100
            KhronoUnit.SECOND -> value * 60.0 * 60 * 24 * 365 * 100
            KhronoUnit.MINUTE -> value * 60.0 * 24 * 365 * 100
            KhronoUnit.HOUR -> value * 24 * 365 * 100
            KhronoUnit.HALF_DAY -> value * 2 * 365 * 100
            KhronoUnit.DAY -> value * 365 * 100
            KhronoUnit.WEEK -> (value * 365 * 100) / 7.0
            KhronoUnit.YEAR -> value * 100
            KhronoUnit.DECADE -> value * 10
            KhronoUnit.CENTURY -> value
            KhronoUnit.MILLENNIUM -> value / 10.0
            KhronoUnit.FOREVER -> KhronoUnit.FOREVER_CONSTANT
        }
    }
}