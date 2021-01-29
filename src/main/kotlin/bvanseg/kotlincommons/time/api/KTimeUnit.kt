package bvanseg.kotlincommons.time.api

import bvanseg.kotlincommons.util.project.Experimental

/**
 *
 * @param max The maximum number of cycles this unit can have relative to the super unit. For example, a second
 * is a sub-unit of a minute, and it takes 60 seconds to make up 1 minute, therefore, the maximum number of cycles for
 * the second is 60.
 *
 * Note, that this only applies to non-calendar units. Calendar units such as days default to 1.
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
enum class KTimeUnit(val max: Long = 1) {
    NANOSECOND(1000),
    MICROSECOND(1000),
    MILLISECOND(1000),
    SECOND(60),
    MINUTE(60),
    HOUR(24),
    DAY,
    WEEK,
    YEAR,
    DECADE,
    CENTURY,
    MILLENNIUM;

    /**
     * Converts a given value to the other unit.
     *
     * @param value The numerical value to convert to the given unit.
     * @param unit The [KTimeUnit] to convert the [value] to.
     *
     * @throws IllegalArgumentException If the given [value] is NaN, infinite, or negative.
     */
    fun convertTo(value: Double, unit: KTimeUnit): Double {

        // No need to transform that which is already 0. Saves processing time.
        if (value == 0.0) return 0.0

        // Handle edge cases of the given value.
        when {
            value.isNaN() -> throw IllegalArgumentException("Expected a valid time value but got $value instead.")
            value.isInfinite() -> throw IllegalArgumentException("Expected a finite value but got $value instead.")
            value < 0 -> throw IllegalArgumentException("Time value can not be negative: $value.")
        }

        return when (this) {
            NANOSECOND -> NanosecondTransformer.transform(value, unit)
            MICROSECOND -> MicrosecondTransformer.transform(value, unit)
            MILLISECOND -> MillisecondTransformer.transform(value, unit)
            SECOND -> SecondTransformer.transform(value, unit)
            MINUTE -> MinuteTransformer.transform(value, unit)
            HOUR -> HourTransformer.transform(value, unit)
            DAY -> DayTransformer.transform(value, unit)
            WEEK -> WeekTransformer.transform(value, unit)
            YEAR -> YearTransformer.transform(value, unit)
            DECADE -> DecadeTransformer.transform(value, unit)
            CENTURY -> CenturyTransformer.transform(value, unit)
            MILLENNIUM -> MillenniumTransformer.transform(value, unit)
        }
    }

    fun getSubUnit(): KTimeUnit? = values().getOrNull(this.ordinal - 1)
    fun getSuperUnit(): KTimeUnit? = values().getOrNull(this.ordinal + 1)
}