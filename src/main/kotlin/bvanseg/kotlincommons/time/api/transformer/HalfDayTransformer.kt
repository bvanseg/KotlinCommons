package bvanseg.kotlincommons.time.api.transformer

import bvanseg.kotlincommons.time.api.KhronoUnit

/**
 * @author Boston Vanseghi
 * @since 2.7.1
 */
object HalfDayTransformer : KTimeTransformer {
    override fun transform(value: Double, unit: KhronoUnit): Double {
        return when (unit) {
            KhronoUnit.NEVER -> KhronoUnit.NEVER_CONSTANT
            KhronoUnit.NANOSECOND -> value * 1_000_000.0 * 1000 * 60 * 60 * 12
            KhronoUnit.MICROSECOND -> value * 1_000.0 * 1000 * 60 * 60 * 12
            KhronoUnit.MILLISECOND -> value * 1000.0 * 60 * 60 * 12
            KhronoUnit.SECOND -> value * 60.0 * 60 * 12
            KhronoUnit.MINUTE -> value * 60.0 * 12
            KhronoUnit.HOUR -> value * 12
            KhronoUnit.HALF_DAY -> value
            KhronoUnit.DAY -> value / 2
            KhronoUnit.WEEK -> value / (7.0 * 2)
            KhronoUnit.YEAR -> value / (365.0 * 2)
            KhronoUnit.DECADE -> value / (365.0 * 10 * 2)
            KhronoUnit.CENTURY -> value / (365.0 * 100 * 2)
            KhronoUnit.MILLENNIUM -> value / (365.0 * 1000 * 2)
            KhronoUnit.FOREVER -> KhronoUnit.FOREVER_CONSTANT
        }
    }
}