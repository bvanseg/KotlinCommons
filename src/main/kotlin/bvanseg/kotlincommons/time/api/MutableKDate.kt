package bvanseg.kotlincommons.time.api

import bvanseg.kotlincommons.time.api.operator.plus
import bvanseg.kotlincommons.util.comparable.clamp

/**
 * @author Boston Vanseghi
 * @since 2.8.0
 */
class MutableKDate(
    d: Double = 0.0,
    mth: Int,
    yr: Double = 0.0
): KDate(d, mth, yr) {
    override lateinit var day: Khrono
    override lateinit var month: KMonth
    override lateinit var monthKhrono: Khrono
    override lateinit var year: Khrono

    /**
     * @param d Number of days.
     * @param mth The [KMonth] premium.
     * @param yr The year.
     */
    constructor(d: Double, mth: KMonth, yr: Double) : this(d, mth.monthValue, yr)

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
}