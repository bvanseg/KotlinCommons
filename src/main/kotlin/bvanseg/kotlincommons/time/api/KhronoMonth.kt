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