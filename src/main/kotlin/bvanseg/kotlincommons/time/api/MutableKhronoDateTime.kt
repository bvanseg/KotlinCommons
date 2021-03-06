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

import java.time.LocalDateTime

/**
 * @author Boston Vanseghi
 * @since 2.8.0
 */
class MutableKhronoDateTime(override val date: MutableKhronoDate, override val time: MutableKhronoTime) :
    KhronoDateTime(date, time) {

    constructor(
        day: Double,
        month: Int,
        year: Double
    ) : this(MutableKhronoDate(day, month, year), MutableKhronoTime())

    constructor(
        day: Double,
        month: Int,
        year: Double,
        hour: Double = 0.0,
        minute: Double = 0.0,
        second: Double = 0.0,
        millisecond: Double = 0.0,
        microsecond: Double = 0.0,
        nanosecond: Double = 0.0
    ) : this(
        MutableKhronoDate(day, month, year),
        MutableKhronoTime(hour, minute, second, millisecond, microsecond, nanosecond)
    )

    constructor(
        day: Double,
        month: KhronoMonth,
        year: Double,
        hour: Double = 0.0,
        minute: Double = 0.0,
        second: Double = 0.0,
        millisecond: Double = 0.0,
        microsecond: Double = 0.0,
        nanosecond: Double = 0.0
    ) : this(
        MutableKhronoDate(day, month.monthValue, year),
        MutableKhronoTime(hour, minute, second, millisecond, microsecond, nanosecond)
    )

    override val day: MutableKhrono
        get() = date.day
    override val month: MutableKhrono
        get() = date.monthKhrono
    override val year: MutableKhrono
        get() = date.year

    override val hour: MutableKhrono
        get() = time.hour
    override val minute: MutableKhrono
        get() = time.minute
    override val second: MutableKhrono
        get() = time.second
    override val millisecond: MutableKhrono
        get() = time.millisecond
    override val microsecond: MutableKhrono
        get() = time.microsecond
    override val nanosecond: MutableKhrono
        get() = time.nanosecond

    override val asNanos: Double
        get() = date.asNanos + time.asNanos
    override val asMicros: Double
        get() = date.asMicros + time.asMicros
    override val asMillis: Double
        get() = date.asMillis + time.asMillis
    override val asSeconds: Double
        get() = date.asSeconds + time.asSeconds
    override val asMinutes: Double
        get() = date.asMinutes + time.asMinutes
    override val asHours: Double
        get() = date.asHours + time.asHours
    override val asHalfDays: Double
        get() = date.asHalfDays + time.asHalfDays
    override val asDays: Double
        get() = date.asDays + time.asDays
    override val asWeeks: Double
        get() = date.asWeeks + time.asWeeks
    override val asYears: Double
        get() = date.asYears + time.asYears
    override val asDecades: Double
        get() = date.asDecades + time.asDecades
    override val asCenturies: Double
        get() = date.asCenturies + time.asCenturies
    override val asMillenniums: Double
        get() = date.asMillenniums + time.asMillenniums

    companion object {
        fun yesterday(): MutableKhronoDateTime = now().apply { day - 1 }
        fun now(): MutableKhronoDateTime = LocalDateTime.now().toMutableKhronoDateTime()
        fun tomorrow(): MutableKhronoDateTime = now().apply { day + 1 }

        fun afternoon(): MutableKhronoDateTime =
            MutableKhronoDateTime(MutableKhronoDate.now(), MutableKhronoTime(hr = 12.0))

        fun midnight(): MutableKhronoDateTime = MutableKhronoDateTime(MutableKhronoDate.now(), MutableKhronoTime())
    }
}