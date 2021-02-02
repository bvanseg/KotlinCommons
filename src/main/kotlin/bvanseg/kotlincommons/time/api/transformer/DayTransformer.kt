package bvanseg.kotlincommons.time.api.transformer

import bvanseg.kotlincommons.time.api.KhronoUnit

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
object DayTransformer : KTimeTransformer {
    override fun transform(value: Double, unit: KhronoUnit): Double {
        return when (unit) {
            KhronoUnit.NEVER -> KhronoUnit.NEVER_CONSTANT
            KhronoUnit.NANOSECOND -> value * 1_000_000.0 * 1000 * 60 * 60 * 24
            KhronoUnit.MICROSECOND -> value * 1_000.0 * 1000 * 60 * 60 * 24
            KhronoUnit.MILLISECOND -> value * 1000.0 * 60 * 60 * 24
            KhronoUnit.SECOND -> value * 60.0 * 60 * 24
            KhronoUnit.MINUTE -> value * 60.0 * 24
            KhronoUnit.HOUR -> value * 24
            KhronoUnit.HALF_DAY -> value * 2
            KhronoUnit.DAY -> value
            KhronoUnit.WEEK -> value / 7.0
            KhronoUnit.YEAR -> value / 365.0
            KhronoUnit.DECADE -> value / (365.0 * 10)
            KhronoUnit.CENTURY -> value / (365.0 * 100)
            KhronoUnit.MILLENNIUM -> value / (365.0 * 1000)
            KhronoUnit.FOREVER -> KhronoUnit.FOREVER_CONSTANT
        }
    }
}