package bvanseg.kotlincommons.time.api.transformer

import bvanseg.kotlincommons.time.api.KhronoUnit

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
object HourTransformer : KTimeTransformer {
    override fun transform(value: Double, unit: KhronoUnit): Double {
        return when (unit) {
            KhronoUnit.NEVER -> KhronoUnit.NEVER_CONSTANT
            KhronoUnit.NANOSECOND -> value * 1_000_000.0 * 1000 * 60 * 60
            KhronoUnit.MICROSECOND -> value * 1_000.0 * 1000 * 60 * 60
            KhronoUnit.MILLISECOND -> value * 1000.0 * 60 * 60
            KhronoUnit.SECOND -> value * 60.0 * 60
            KhronoUnit.MINUTE -> value * 60.0
            KhronoUnit.HOUR -> value
            KhronoUnit.HALF_DAY -> value / 12.0
            KhronoUnit.DAY -> value / 24.0
            KhronoUnit.WEEK -> value / (24.0 * 7)
            KhronoUnit.YEAR -> value / (24.0 * 365)
            KhronoUnit.DECADE -> value / (24.0 * 365 * 10)
            KhronoUnit.CENTURY -> value / (24.0 * 365 * 100)
            KhronoUnit.MILLENNIUM -> value / (24.0 * 365 * 1000)
            KhronoUnit.FOREVER -> KhronoUnit.FOREVER_CONSTANT
        }
    }
}