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

import bvanseg.kotlincommons.util.HashCodeBuilder
import bvanseg.kotlincommons.util.comparable.clamp
import java.time.LocalDate

/**
 * @author Boston Vanseghi
 * @since 2.8.0
 */
open class KhronoDate(
    d: Double = 0.0,
    mth: Int,
    yr: Double = 0.0
) : KhronoType, Comparable<KhronoDate> {
    open val day: Khrono
    open val month: KhronoMonth
    open val monthKhrono: Khrono
    open val year: Khrono

    /**
     * @param d Number of days.
     * @param mth The [KhronoMonth] premium.
     * @param yr The year.
     */
    constructor(d: Double, mth: KhronoMonth, yr: Double) : this(d, mth.monthValue, yr)

    init {
        var day = d
        var runningMonthValue = mth
        var currentMonth = getMonthFromValue(runningMonthValue)
        while (day > currentMonth.days) {
            day -= currentMonth.days
            currentMonth = getMonthFromValue(++runningMonthValue)
        }

        this.day = day.days
        this.month = currentMonth
        this.monthKhrono = currentMonth.toKhrono()
        this.year = yr.years + (mth / 13)
    }

    protected fun getMonthFromValue(value: Int): KhronoMonth {
        val v = clamp(value - 1, 0, Int.MAX_VALUE)
        return KhronoMonth.values()[v % 12]
    }

    override val asNanos: Double by lazy { Khrono.combineAll(KhronoUnit.NANOSECOND, day, monthKhrono, year).value }
    open val asMicros: Double by lazy { Khrono.combineAll(KhronoUnit.MICROSECOND, day, monthKhrono, year).value }
    open val asMillis: Double by lazy { Khrono.combineAll(KhronoUnit.MILLISECOND, day, monthKhrono, year).value }
    open val asSeconds: Double by lazy { Khrono.combineAll(KhronoUnit.SECOND, day, monthKhrono, year).value }
    open val asMinutes: Double by lazy { Khrono.combineAll(KhronoUnit.MINUTE, day, monthKhrono, year).value }
    open val asHours: Double by lazy { Khrono.combineAll(KhronoUnit.HOUR, day, monthKhrono, year).value }
    open val asHalfDays: Double by lazy { Khrono.combineAll(KhronoUnit.HALF_DAY, day, monthKhrono, year).value }
    open val asDays: Double by lazy { Khrono.combineAll(KhronoUnit.DAY, day, monthKhrono, year).value }
    open val asWeeks: Double by lazy { Khrono.combineAll(KhronoUnit.WEEK, day, monthKhrono, year).value }
    open val asYears: Double by lazy { Khrono.combineAll(KhronoUnit.YEAR, day, monthKhrono, year).value }
    open val asDecades: Double by lazy { Khrono.combineAll(KhronoUnit.DECADE, day, monthKhrono, year).value }
    open val asCenturies: Double by lazy { Khrono.combineAll(KhronoUnit.CENTURY, day, monthKhrono, year).value }
    open val asMillenniums: Double by lazy { Khrono.combineAll(KhronoUnit.MILLENNIUM, day, monthKhrono, year).value }

    fun toLocalDate(): LocalDate = LocalDate.of(year.toInt(), month.monthValue, day.toInt())

    fun toMutable(): MutableKhronoDate = MutableKhronoDate(day.value, month.monthValue, year.value)

    fun dayBefore(): KhronoDate = toMutable().apply { day -= 1 }
    fun dayAfter(): KhronoDate = toMutable().apply { day += 1 }

    fun daysUntil(date: KhronoDate): Double = date.asDays - this.asDays
    fun daysSince(date: KhronoDate): Double = this.asDays - date.asDays

    override fun isBefore(upperBound: KhronoType): Boolean = this.asNanos < upperBound.asNanos
    override fun isBeforeOrAt(upperBound: KhronoType): Boolean = this.asNanos <= upperBound.asNanos

    override fun isAfter(lowerBound: KhronoType): Boolean = this.asNanos > lowerBound.asNanos
    override fun isAtOrAfter(lowerBound: KhronoType): Boolean = this.asNanos >= lowerBound.asNanos

    override fun toString(): String = "${month.monthValue}/${day.toLong()}/${year.toLong()}"

    override fun equals(other: Any?): Boolean {
        if (other !is KhronoDate) {
            return false
        }

        if (this.day != other.day) return false
        if (this.month != other.month) return false
        if (this.monthKhrono != other.monthKhrono) return false
        if (this.year != other.year) return false

        return true
    }

    override fun hashCode(): Int = HashCodeBuilder.builder(this::class)
        .append(this.day)
        .append(this.month)
        .append(this.monthKhrono)
        .append(this.year)
        .hashCode()

    override fun compareTo(other: KhronoDate): Int = when {
        this.asNanos < other.asNanos -> -1
        this.asNanos > other.asNanos -> 1
        else -> 0
    }

    // OPERATORS

    operator fun plus(other: KhronoTime) = KhronoDateTime(this, other)

    companion object {
        fun yesterday(): KhronoDate = now().toMutable().apply { day -= 1 }
        fun now(): KhronoDate = LocalDate.now().toKhronoDate()
        fun tomorrow(): KhronoDate = now().toMutable().apply { day += 1 }
    }
}