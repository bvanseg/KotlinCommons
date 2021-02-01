package bvanseg.kotlincommons.time.api.transformer

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
import bvanseg.kotlincommons.time.api.KTimeUnit

object WeekTransformer : KTimeTransformer {
    override fun transform(value: Double, unit: KTimeUnit): Double {
        return when (unit) {
            KTimeUnit.NANOSECOND -> value * 1_000_000.0 * 1000 * 60 * 60 * 24 * 7
            KTimeUnit.MICROSECOND -> value * 1_000.0 * 1000 * 60 * 60 * 24 * 7
            KTimeUnit.MILLISECOND -> value * 1000.0 * 60 * 60 * 24 * 7
            KTimeUnit.SECOND -> value * 60.0 * 60 * 24 * 7
            KTimeUnit.MINUTE -> value * 60.0 * 24 * 7
            KTimeUnit.HOUR -> value * 24 * 7
            KTimeUnit.HALF_DAY -> value * 7 * 2
            KTimeUnit.DAY -> value * 7
            KTimeUnit.WEEK -> value
            KTimeUnit.YEAR -> (value * 7) / 365.0
            KTimeUnit.DECADE -> (value * 7) / (365.0 * 10)
            KTimeUnit.CENTURY -> (value * 7) / (365.0 * 100)
            KTimeUnit.MILLENNIUM -> (value * 7) / (365.0 * 1000)
            KTimeUnit.UNKNOWN -> KTimeUnit.UNKNOWN_CONSTANT
        }
    }
}