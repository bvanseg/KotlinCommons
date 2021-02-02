package bvanseg.kotlincommons.time.api

import bvanseg.kotlincommons.time.api.transformer.CenturyTransformer
import bvanseg.kotlincommons.time.api.transformer.DayTransformer
import bvanseg.kotlincommons.time.api.transformer.DecadeTransformer
import bvanseg.kotlincommons.time.api.transformer.HalfDayTransformer
import bvanseg.kotlincommons.time.api.transformer.HourTransformer
import bvanseg.kotlincommons.time.api.transformer.MicrosecondTransformer
import bvanseg.kotlincommons.time.api.transformer.MillenniumTransformer
import bvanseg.kotlincommons.time.api.transformer.MillisecondTransformer
import bvanseg.kotlincommons.time.api.transformer.MinuteTransformer
import bvanseg.kotlincommons.time.api.transformer.NanosecondTransformer
import bvanseg.kotlincommons.time.api.transformer.SecondTransformer
import bvanseg.kotlincommons.time.api.transformer.WeekTransformer
import bvanseg.kotlincommons.time.api.transformer.YearTransformer
import java.time.temporal.ChronoUnit

/**
 *
 * @param max The maximum number of cycles this unit can have relative to the super unit. For example, a second
 * is a sub-unit of a minute, and it takes 60 seconds to make up 1 minute, therefore, the maximum number of cycles for
 * the second is 60.
 *
 * Note, that the above only applies to non-calendar units. Calendar units such as days default to 1.
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
enum class KhronoUnit(val max: Long = 1) {
    NEVER,
    NANOSECOND(1000),
    MICROSECOND(1000),
    MILLISECOND(1000),
    SECOND(60),
    MINUTE(60),
    HOUR(24),
    HALF_DAY,
    DAY,
    WEEK,
    YEAR,
    DECADE,
    CENTURY,
    MILLENNIUM,
    FOREVER;

    companion object {
        const val NEVER_CONSTANT = Double.MIN_VALUE
        const val FOREVER_CONSTANT = Double.MAX_VALUE
    }

    /**
     * Converts a given value to the other unit.
     *
     * @param value The numerical value to convert to the given unit.
     * @param unit The [KhronoUnit] to convert the [value] to.
     *
     * @throws IllegalArgumentException If the given [value] is NaN, infinite, or negative.
     */
    fun convertTo(value: Double, unit: KhronoUnit): Double {

        // No need to transform that which is already 0. Saves processing time.
        if (value == 0.0) return 0.0

        // Handle edge cases of the given value.
        when {
            value.isNaN() -> throw IllegalArgumentException("Expected a valid time value but got $value instead.")
            value.isInfinite() -> throw IllegalArgumentException("Expected a finite value but got $value instead.")
            value < 0 -> throw IllegalArgumentException("Time value can not be negative: $value.")
        }

        return when (this) {
            NEVER -> NEVER_CONSTANT
            NANOSECOND -> NanosecondTransformer.transform(value, unit)
            MICROSECOND -> MicrosecondTransformer.transform(value, unit)
            MILLISECOND -> MillisecondTransformer.transform(value, unit)
            SECOND -> SecondTransformer.transform(value, unit)
            MINUTE -> MinuteTransformer.transform(value, unit)
            HOUR -> HourTransformer.transform(value, unit)
            HALF_DAY -> HalfDayTransformer.transform(value, unit)
            DAY -> DayTransformer.transform(value, unit)
            WEEK -> WeekTransformer.transform(value, unit)
            YEAR -> YearTransformer.transform(value, unit)
            DECADE -> DecadeTransformer.transform(value, unit)
            CENTURY -> CenturyTransformer.transform(value, unit)
            MILLENNIUM -> MillenniumTransformer.transform(value, unit)
            FOREVER -> FOREVER_CONSTANT
        }
    }

    fun getSubUnit(): KhronoUnit = values().getOrNull(this.ordinal - 1) ?: NEVER
    fun getSuperUnit(): KhronoUnit = values().getOrNull(this.ordinal + 1) ?: FOREVER

    fun toChronoUnit(): ChronoUnit? = when(this) {
        NEVER -> null
        NANOSECOND -> ChronoUnit.NANOS
        MICROSECOND -> ChronoUnit.MICROS
        MILLISECOND -> ChronoUnit.MILLIS
        SECOND -> ChronoUnit.SECONDS
        MINUTE -> ChronoUnit.MINUTES
        HOUR -> ChronoUnit.HOURS
        HALF_DAY -> ChronoUnit.HALF_DAYS
        DAY -> ChronoUnit.DAYS
        WEEK -> ChronoUnit.WEEKS
        YEAR -> ChronoUnit.YEARS
        DECADE -> ChronoUnit.DECADES
        CENTURY -> ChronoUnit.CENTURIES
        MILLENNIUM -> ChronoUnit.MILLENNIA
        FOREVER -> ChronoUnit.FOREVER
    }
}