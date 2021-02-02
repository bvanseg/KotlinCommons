package bvanseg.kotlincommons.time.api.transformer

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
import bvanseg.kotlincommons.time.api.KhronoUnit

object WeekTransformer : KTimeTransformer {
    override fun transform(value: Double, unit: KhronoUnit): Double {
        return when (unit) {
            KhronoUnit.NEVER -> KhronoUnit.NEVER_CONSTANT
            KhronoUnit.NANOSECOND -> value * 1_000_000.0 * 1000 * 60 * 60 * 24 * 7
            KhronoUnit.MICROSECOND -> value * 1_000.0 * 1000 * 60 * 60 * 24 * 7
            KhronoUnit.MILLISECOND -> value * 1000.0 * 60 * 60 * 24 * 7
            KhronoUnit.SECOND -> value * 60.0 * 60 * 24 * 7
            KhronoUnit.MINUTE -> value * 60.0 * 24 * 7
            KhronoUnit.HOUR -> value * 24 * 7
            KhronoUnit.HALF_DAY -> value * 7 * 2
            KhronoUnit.DAY -> value * 7
            KhronoUnit.WEEK -> value
            KhronoUnit.YEAR -> (value * 7) / 365.0
            KhronoUnit.DECADE -> (value * 7) / (365.0 * 10)
            KhronoUnit.CENTURY -> (value * 7) / (365.0 * 100)
            KhronoUnit.MILLENNIUM -> (value * 7) / (365.0 * 1000)
            KhronoUnit.FOREVER -> KhronoUnit.FOREVER_CONSTANT
        }
    }
}