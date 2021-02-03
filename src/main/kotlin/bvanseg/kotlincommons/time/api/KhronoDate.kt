package bvanseg.kotlincommons.time.api

import bvanseg.kotlincommons.time.api.operator.minus
import bvanseg.kotlincommons.time.api.operator.plus
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
): Comparable<KhronoDate> {
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

    open val asNanos: Double by lazy { Khrono.combineAll(KhronoUnit.NANOSECOND, day, monthKhrono, year).value }
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
    fun isBetween(lowerBound: KhronoDate, upperBound: KhronoDate): Boolean =
        this.asMillis >= lowerBound.asMillis && this.asMillis <= upperBound.asMillis

    override fun toString(): String = "${month.monthValue}/${day.toLong()}/${year.toLong()}"

    override fun compareTo(other: KhronoDate): Int = when {
        this.asNanos < other.asNanos -> -1
        this.asNanos > other.asNanos -> 1
        else -> 0
    }

    companion object {
        fun yesterday(): KhronoDate = now().toMutable().apply { day -= 1 }
        fun now(): KhronoDate = LocalDate.now().toKhronoDate()
        fun tomorrow(): KhronoDate = now().toMutable().apply { day += 1 }
    }
}