package bvanseg.kotlincommons.time.api

import bvanseg.kotlincommons.util.comparable.clamp

/**
 * @author Boston Vanseghi
 * @since 2.8.0
 */
enum class KhronoMonth(val monthValue: Int, val days: Int) {
    JANUARY(1, 31),
    FEBRUARY(2, 28),
    MARCH(3, 31),
    APRIL(4, 30),
    MAY(5, 31),
    JUNE(6, 30),
    JULY(7, 31),
    AUGUST(8, 31),
    SEPTEMBER(9, 30),
    OCTOBER(10, 31),
    NOVEMBER(11, 30),
    DECEMBER(12, 31);

    companion object {
        fun getFromDayOfYear(dayOfYear: Int): KhronoMonth {
            var d = clamp(dayOfYear, 1, 365)
            var month = JANUARY

            while (d > month.days) {
                d -= month.days
                month = values()[month.ordinal + 1]
            }

            return month
        }

        fun getDaysUpTo(month: KhronoMonth): Int {
            var d = 0
            var currentMonth = JANUARY

            while (currentMonth < month) {
                d += currentMonth.days
                currentMonth = values()[currentMonth.ordinal + 1]
            }
            return d
        }
    }

    fun previousMonth(): KhronoMonth = values().getOrNull(this.ordinal - 1) ?: values()[values().size - 1]
    fun nextMonth(): KhronoMonth = values().getOrNull(this.ordinal + 1) ?: values()[0]

    fun toKhrono(): Khrono = Khrono(getDaysUpTo(this).toDouble(), KhronoUnit.DAY)
}