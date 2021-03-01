/*
 * MIT License
 *
 * Copyright (c) 2021 Boston Vanseghi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package bvanseg.kotlincommons.time.api

import bvanseg.kotlincommons.lang.checks.Checks
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
import java.util.concurrent.TimeUnit

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
enum class KhronoUnit(val code: String, val max: Double = 1.0, val calendarUnit: Double = 1.0) {
    NEVER("nvr"),
    NANOSECOND("nan", 1000.0),
    MICROSECOND("mic", 1000.0),
    MILLISECOND("mil", 1000.0),
    SECOND("sec", 60.0),
    MINUTE("min", 60.0),
    HOUR("hr", 24.0),
    HALF_DAY("hd",2.0, calendarUnit = 23.933333333333333333 / 2.0), // Calendar unit relative to hours
    DAY("dy", 7.0, calendarUnit = 23.933333333333333333), // Calendar unit relative to hours
    WEEK("wk", 3.999999999999912, calendarUnit = 7.609212479166666666666), // Calendar unit relative to days
    YEAR("yr", 10.0, calendarUnit = 365.242199), // Calendar unit relative to days
    DECADE("dec", 10.0, calendarUnit = 10 * YEAR.calendarUnit),
    CENTURY("cnt", 10.0, calendarUnit = 100 * YEAR.calendarUnit),
    MILLENNIUM("mln", calendarUnit = 1000 * YEAR.calendarUnit),
    FOREVER("fvr");

    companion object {
        const val NEVER_CONSTANT = Double.MIN_VALUE
        const val FOREVER_CONSTANT = Double.MAX_VALUE

        fun fromCode(code: String): KhronoUnit = when (code.toLowerCase()) {
            NEVER.code -> NEVER
            NANOSECOND.code -> NANOSECOND
            MICROSECOND.code -> MICROSECOND
            MILLISECOND.code -> MILLISECOND
            SECOND.code -> SECOND
            MINUTE.code -> MINUTE
            HOUR.code -> HOUR
            HALF_DAY.code -> HALF_DAY
            DAY.code -> DAY
            WEEK.code -> WEEK
            YEAR.code -> YEAR
            DECADE.code -> DECADE
            CENTURY.code -> CENTURY
            MILLENNIUM.code -> MILLENNIUM
            FOREVER.code -> FOREVER
            else -> throw IllegalArgumentException("Code '$code' is not a valid khrono unit code!")
        }
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

        Checks.isFinite(value)
        Checks.isWholeNumber(value)

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

    fun toTimeUnit(): TimeUnit? = when (this) {
        NANOSECOND -> TimeUnit.NANOSECONDS
        MICROSECOND -> TimeUnit.MICROSECONDS
        MILLISECOND -> TimeUnit.MILLISECONDS
        SECOND -> TimeUnit.SECONDS
        MINUTE -> TimeUnit.MINUTES
        HOUR -> TimeUnit.HOURS
        DAY -> TimeUnit.DAYS
        else -> null
    }

    fun toChronoUnit(): ChronoUnit? = when (this) {
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