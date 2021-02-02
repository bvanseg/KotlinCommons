package bvanseg.kotlincommons.time.api.transformer

import bvanseg.kotlincommons.time.api.KhronoUnit

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
object DecadeTransformer : KTimeTransformer {
    override fun transform(value: Double, unit: KhronoUnit): Double {
        return when (unit) {
            KhronoUnit.NEVER -> KhronoUnit.NEVER_CONSTANT
            KhronoUnit.NANOSECOND -> value * 1_000_000.0 * 1000 * 60 * 60 * 24 * 365 * 10
            KhronoUnit.MICROSECOND -> value * 1_000.0 * 1000 * 60 * 60 * 24 * 365 * 10
            KhronoUnit.MILLISECOND -> value * 1000.0 * 60 * 60 * 24 * 365 * 10
            KhronoUnit.SECOND -> value * 60.0 * 60 * 24 * 365 * 10
            KhronoUnit.MINUTE -> value * 60.0 * 24 * 365 * 10
            KhronoUnit.HOUR -> value * 24 * 365 * 10
            KhronoUnit.HALF_DAY -> value * 2 * 365 * 10
            KhronoUnit.DAY -> value * 365 * 10
            KhronoUnit.WEEK -> (value * 365 * 10) / 7.0
            KhronoUnit.YEAR -> value * 10
            KhronoUnit.DECADE -> value
            KhronoUnit.CENTURY -> value / 10.0
            KhronoUnit.MILLENNIUM -> value / 100.0
            KhronoUnit.FOREVER -> KhronoUnit.FOREVER_CONSTANT
        }
    }
}