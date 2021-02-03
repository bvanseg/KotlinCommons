package bvanseg.kotlincommons.time.api

import bvanseg.kotlincommons.time.api.operator.minus
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

    override var day: MutableKhrono = MutableKhrono.EMPTY
        set(value) {
            value.onChange = {
                updateCalculations()
                handleOverflow()
            }
            field = value
            updateCalculations()
            handleOverflow()
        }

    override var month: KhronoMonth = KhronoMonth.JANUARY
        private set

    override var monthKhrono: MutableKhrono = MutableKhrono.EMPTY
        set(value) {
            value.onChange = {
                updateCalculations()
                handleOverflow()
            }
            field = value
            updateCalculations()
            handleOverflow()
        }

    override var year: MutableKhrono = MutableKhrono.EMPTY
        set(value) {
            value.onChange = {
                updateCalculations()
                handleOverflow()
            }
            field = value
            updateCalculations()
            handleOverflow()
        }

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

        this.day = day.days.toMutable()
        this.month = currentMonth
        this.monthKhrono = month.toKhrono().toMutable()
        this.year = yr.years.toMutable() + (mth / 13)
    }

    override var asNanos: Double = Khrono.combineAll(KhronoUnit.NANOSECOND, day, monthKhrono, year).value
        private set
    override var asMicros: Double = Khrono.combineAll(KhronoUnit.MICROSECOND, day, monthKhrono, year).value
        private set
    override var asMillis: Double = Khrono.combineAll(KhronoUnit.MILLISECOND, day, monthKhrono, year).value
        private set
    override var asSeconds: Double = Khrono.combineAll(KhronoUnit.SECOND, day, monthKhrono, year).value
        private set
    override var asMinutes: Double = Khrono.combineAll(KhronoUnit.MINUTE, day, monthKhrono, year).value
        private set
    override var asHours: Double = Khrono.combineAll(KhronoUnit.HOUR, day, monthKhrono, year).value
        private set
    override var asHalfDays: Double = Khrono.combineAll(KhronoUnit.HALF_DAY, day, monthKhrono, year).value
        private set
    override var asDays: Double = Khrono.combineAll(KhronoUnit.DAY, day, monthKhrono, year).value
        private set
    override var asWeeks: Double = Khrono.combineAll(KhronoUnit.WEEK, day, monthKhrono, year).value
        private set
    override var asYears: Double = Khrono.combineAll(KhronoUnit.YEAR, day, monthKhrono, year).value
        private set
    override var asDecades: Double = Khrono.combineAll(KhronoUnit.DECADE, day, monthKhrono, year).value
        private set
    override var asCenturies: Double = Khrono.combineAll(KhronoUnit.CENTURY, day, monthKhrono, year).value
        private set
    override var asMillenniums: Double = Khrono.combineAll(KhronoUnit.MILLENNIUM, day, monthKhrono, year).value
        private set

    private fun updateCalculations() {
        asNanos = Khrono.combineAll(KhronoUnit.NANOSECOND, day, monthKhrono, year).value
        asMicros = Khrono.combineAll(KhronoUnit.MICROSECOND, day, monthKhrono, year).value
        asMillis = Khrono.combineAll(KhronoUnit.MILLISECOND, day, monthKhrono, year).value
        asSeconds = Khrono.combineAll(KhronoUnit.SECOND, day, monthKhrono, year).value
        asMinutes = Khrono.combineAll(KhronoUnit.MINUTE, day, monthKhrono, year).value
        asHours = Khrono.combineAll(KhronoUnit.HOUR, day, monthKhrono, year).value
        asHalfDays = Khrono.combineAll(KhronoUnit.HALF_DAY, day, monthKhrono, year).value
        asDays = Khrono.combineAll(KhronoUnit.DAY, day, monthKhrono, year).value
        asWeeks = Khrono.combineAll(KhronoUnit.WEEK, day, monthKhrono, year).value
        asYears = Khrono.combineAll(KhronoUnit.YEAR, day, monthKhrono, year).value
        asDecades = Khrono.combineAll(KhronoUnit.DECADE, day, monthKhrono, year).value
        asCenturies = Khrono.combineAll(KhronoUnit.CENTURY, day, monthKhrono, year).value
        asMillenniums = Khrono.combineAll(KhronoUnit.MILLENNIUM, day, monthKhrono, year).value
    }

    private fun handleOverflow() {
        when {
            // Handle case where days are negative.
            day.value <= 0.0 -> {
                while (day.value <= 0) {
                    month = month.previousMonth()
                    monthKhrono.value = month.monthValue.toDouble()
                    day += month.days

                    if (month == KhronoMonth.DECEMBER) {
                        year -= 1
                    }
                }
            }
            day.value > month.days -> {
                while (day.value > month.days) {
                    day -= month.days
                    month = month.nextMonth()
                    monthKhrono.value = month.monthValue.toDouble()

                    if (month == KhronoMonth.JANUARY) {
                        year += 1
                    }
                }
            }
        }
    }

    companion object {
        fun yesterday(): MutableKhronoDate = KhronoDate.now().toMutable().apply { day -= 1 }
        fun now(): MutableKhronoDate = LocalDate.now().toMutableKhronoDate()
        fun tomorrow(): MutableKhronoDate = KhronoDate.now().toMutable().apply { day += 1 }
    }
}