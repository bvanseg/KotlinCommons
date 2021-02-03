package bvanseg.kotlincommons.time.api

import bvanseg.kotlincommons.time.api.operator.plus
import java.time.LocalDate

/**
 * @author Boston Vanseghi
 * @since 2.8.0
 */
class MutableKhronoDate(
    d: Double = 0.0,
    mth: Int,
    yr: Double = 0.0
) : KhronoDate(d, mth, yr) {
    override lateinit var day: Khrono
    override lateinit var month: KhronoMonth
    override lateinit var monthKhrono: Khrono
    override lateinit var year: Khrono

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
        this.monthKhrono = month.toKhrono()
        this.year = yr.years + (mth / 13)
    }

    // Because this type is mutable, we must override these and recalculate every getter in case day, month, or
    // year khronos change their values.
    override val asNanos: Double
        get() = Khrono.combineAll(KhronoUnit.NANOSECOND, day, monthKhrono, year).value
    override val asMicros: Double
        get() = Khrono.combineAll(KhronoUnit.MICROSECOND, day, monthKhrono, year).value
    override val asMillis: Double
        get() = Khrono.combineAll(KhronoUnit.MILLISECOND, day, monthKhrono, year).value
    override val asSeconds: Double
        get() = Khrono.combineAll(KhronoUnit.SECOND, day, monthKhrono, year).value
    override val asMinutes: Double
        get() = Khrono.combineAll(KhronoUnit.MINUTE, day, monthKhrono, year).value
    override val asHours: Double
        get() = Khrono.combineAll(KhronoUnit.HOUR, day, monthKhrono, year).value
    override val asHalfDays: Double
        get() = Khrono.combineAll(KhronoUnit.HALF_DAY, day, monthKhrono, year).value
    override val asDays: Double
        get() = Khrono.combineAll(KhronoUnit.DAY, day, monthKhrono, year).value
    override val asWeeks: Double
        get() = Khrono.combineAll(KhronoUnit.WEEK, day, monthKhrono, year).value
    override val asYears: Double
        get() = Khrono.combineAll(KhronoUnit.YEAR, day, monthKhrono, year).value
    override val asDecades: Double
        get() = Khrono.combineAll(KhronoUnit.DECADE, day, monthKhrono, year).value
    override val asCenturies: Double
        get() = Khrono.combineAll(KhronoUnit.CENTURY, day, monthKhrono, year).value
    override val asMillenniums: Double
        get() = Khrono.combineAll(KhronoUnit.MILLENNIUM, day, monthKhrono, year).value

    companion object {
        fun now(): MutableKhronoDate = LocalDate.now().toMutableKhronoDate()
    }
}