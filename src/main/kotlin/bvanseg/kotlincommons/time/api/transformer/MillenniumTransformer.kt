package bvanseg.kotlincommons.time.api.transformer

import bvanseg.kotlincommons.time.api.KhronoUnit

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
object MillenniumTransformer : KTimeTransformer {
    override fun transform(value: Double, unit: KhronoUnit): Double {
        return when (unit) {
            KhronoUnit.NEVER -> KhronoUnit.NEVER_CONSTANT
            KhronoUnit.NANOSECOND -> value * 1_000_000.0 * 1000 * 60 * 60 * 24 * 365 * 1000
            KhronoUnit.MICROSECOND -> value * 1_000.0 * 1000 * 60 * 60 * 24 * 365 * 1000
            KhronoUnit.MILLISECOND -> value * 1000.0 * 60 * 60 * 24 * 365 * 1000
            KhronoUnit.SECOND -> value * 60.0 * 60 * 24 * 365 * 1000
            KhronoUnit.MINUTE -> value * 60.0 * 24 * 365 * 1000
            KhronoUnit.HOUR -> value * 24 * 365 * 1000
            KhronoUnit.HALF_DAY -> value * 2 * 365 * 1000
            KhronoUnit.DAY -> value * 365 * 1000
            KhronoUnit.WEEK -> (value * 365 * 1000) / 7.0
            KhronoUnit.YEAR -> value * 1000
            KhronoUnit.DECADE -> value * 100
            KhronoUnit.CENTURY -> value * 10
            KhronoUnit.MILLENNIUM -> value
            KhronoUnit.FOREVER -> KhronoUnit.FOREVER_CONSTANT
        }
    }
}