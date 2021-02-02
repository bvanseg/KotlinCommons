package bvanseg.kotlincommons.time.api.transformer

import bvanseg.kotlincommons.time.api.KhronoUnit

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
object NanosecondTransformer : KTimeTransformer {
    override fun transform(value: Double, unit: KhronoUnit): Double {
        return when (unit) {
            KhronoUnit.NEVER -> KhronoUnit.NEVER_CONSTANT
            KhronoUnit.NANOSECOND -> value
            KhronoUnit.MICROSECOND -> value / 1_000.0
            KhronoUnit.MILLISECOND -> value / 1_000_000.0
            KhronoUnit.SECOND -> value / (1_000_000.0 * 1000)
            KhronoUnit.MINUTE -> value / (1_000_000.0 * 1000 * 60)
            KhronoUnit.HOUR -> value / (1_000_000.0 * 1000 * 60 * 60)
            KhronoUnit.HALF_DAY -> value / (1_000_000.0 * 1000 * 60 * 60 * 24 * 2)
            KhronoUnit.DAY -> value / (1_000_000.0 * 1000 * 60 * 60 * 24)
            KhronoUnit.WEEK -> value / (1_000_000.0 * 1000 * 60 * 60 * 24 * 7)
            KhronoUnit.YEAR -> value / (1_000_000.0 * 1000 * 60 * 60 * 24 * 365)
            KhronoUnit.DECADE -> value / (1_000_000.0 * 1000 * 60 * 60 * 24 * 365 * 10)
            KhronoUnit.CENTURY -> value / (1_000_000.0 * 1000 * 60 * 60 * 24 * 365 * 100)
            KhronoUnit.MILLENNIUM -> value / (1_000_000.0 * 1000 * 60 * 60 * 24 * 365 * 1000)
            KhronoUnit.FOREVER -> KhronoUnit.FOREVER_CONSTANT
        }
    }
}