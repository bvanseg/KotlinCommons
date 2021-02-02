package bvanseg.kotlincommons.time.api

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
) {
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
        this.monthKhrono = month.toKhrono()
        this.year = yr.years + (mth / 13)
    }

    protected fun getMonthFromValue(value: Int): KhronoMonth {
        val v = clamp(value - 1, 0, Int.MAX_VALUE)
        return KhronoMonth.values()[v % 12]
    }

    val asNanos: Double by lazy { Khrono.combineAll(KhronoUnit.NANOSECOND, day, monthKhrono, year).value }
    val asMicros: Double by lazy { Khrono.combineAll(KhronoUnit.MICROSECOND, day, monthKhrono, year).value }
    val asMillis: Double by lazy { Khrono.combineAll(KhronoUnit.MILLISECOND, day, monthKhrono, year).value }
    val asSeconds: Double by lazy { Khrono.combineAll(KhronoUnit.SECOND, day, monthKhrono, year).value }
    val asMinutes: Double by lazy { Khrono.combineAll(KhronoUnit.MINUTE, day, monthKhrono, year).value }
    val asHours: Double by lazy { Khrono.combineAll(KhronoUnit.HOUR, day, monthKhrono, year).value }
    val asHalfDays: Double by lazy { Khrono.combineAll(KhronoUnit.HALF_DAY, day, monthKhrono, year).value }
    val asDays: Double by lazy { Khrono.combineAll(KhronoUnit.DAY, day, monthKhrono, year).value }
    val asWeeks: Double by lazy { Khrono.combineAll(KhronoUnit.WEEK, day, monthKhrono, year).value }
    val asYears: Double by lazy { Khrono.combineAll(KhronoUnit.YEAR, day, monthKhrono, year).value }
    val asDecades: Double by lazy { Khrono.combineAll(KhronoUnit.DECADE, day, monthKhrono, year).value }
    val asCenturies: Double by lazy { Khrono.combineAll(KhronoUnit.CENTURY, day, monthKhrono, year).value }
    val asMillenniums: Double by lazy { Khrono.combineAll(KhronoUnit.MILLENNIUM, day, monthKhrono, year).value }

    fun toLocalDate(): LocalDate = LocalDate.of(year.value.toInt(), month.monthValue, day.value.toInt())
}