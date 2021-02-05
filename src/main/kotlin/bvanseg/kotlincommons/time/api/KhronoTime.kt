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
import java.time.Duration
import java.time.LocalTime

/**
 * @author Boston Vanseghi
 * @since 2.8.0
 */
// TODO: Add support for AM/PM.
// TODO: Add support for timezones.
// TODO: Double-check whether or not negatives work.
open class KhronoTime(
    hr: Double = 0.0,
    min: Double = 0.0,
    sec: Double = 0.0,
    millis: Double = 0.0,
    micro: Double = 0.0,
    nano: Double = 0.0
) : KhronoType, Comparable<KhronoTime> {
    open val nanosecond: Khrono
    open val microsecond: Khrono
    open val millisecond: Khrono
    open val second: Khrono
    open val minute: Khrono
    open val hour: Khrono

    init {
        val nanoLeftover = nano % KhronoUnit.NANOSECOND.max
        val nanoOverflow = (nano - nanoLeftover)
        val microFromNano = nanoOverflow / KhronoUnit.NANOSECOND.max
        nanosecond = Khrono(nanoLeftover, KhronoUnit.NANOSECOND)

        val microTotal = micro + microFromNano

        val microLeftover = microTotal % KhronoUnit.MICROSECOND.max
        val microOverflow = (microTotal - microLeftover)
        val millisFromMicro = microOverflow / KhronoUnit.MICROSECOND.max
        microsecond = Khrono(microLeftover, KhronoUnit.MICROSECOND)

        val millisTotal = millis + millisFromMicro

        val millisLeftover = millisTotal % KhronoUnit.MILLISECOND.max
        val millisOverflow = (millisTotal - millisLeftover)
        val secFromMillis = millisOverflow / KhronoUnit.MILLISECOND.max
        millisecond = Khrono(millisLeftover, KhronoUnit.MILLISECOND)

        val secTotal = sec + secFromMillis

        val secLeftover = secTotal % KhronoUnit.SECOND.max
        val secOverflow = (secTotal - secLeftover)
        val minFromSec = secOverflow / KhronoUnit.SECOND.max
        second = Khrono(secLeftover, KhronoUnit.SECOND)

        val minTotal = min + minFromSec

        val minLeftover = minTotal % KhronoUnit.MINUTE.max
        val minOverflow = (minTotal - minLeftover)
        val hourFromMin = minOverflow / KhronoUnit.MINUTE.max
        minute = Khrono(minLeftover, KhronoUnit.MINUTE)

        val hourTotal = hr + hourFromMin

        hour = Khrono(hourTotal, KhronoUnit.HOUR)
    }

    override val asNanos: Double by lazy {
        Khrono.combineAll(
            KhronoUnit.NANOSECOND,
            hour,
            minute,
            second,
            millisecond,
            microsecond,
            nanosecond
        ).value
    }
    open val asMicros: Double by lazy {
        Khrono.combineAll(
            KhronoUnit.MICROSECOND,
            hour,
            minute,
            second,
            millisecond,
            microsecond,
            nanosecond
        ).value
    }
    open val asMillis: Double by lazy {
        Khrono.combineAll(
            KhronoUnit.MILLISECOND,
            hour,
            minute,
            second,
            millisecond,
            microsecond,
            nanosecond
        ).value
    }
    open val asSeconds: Double by lazy {
        Khrono.combineAll(
            KhronoUnit.SECOND,
            hour,
            minute,
            second,
            millisecond,
            microsecond,
            nanosecond
        ).value
    }
    open val asMinutes: Double by lazy {
        Khrono.combineAll(
            KhronoUnit.MINUTE,
            hour,
            minute,
            second,
            millisecond,
            microsecond,
            nanosecond
        ).value
    }
    open val asHours: Double by lazy {
        Khrono.combineAll(
            KhronoUnit.HOUR,
            hour,
            minute,
            second,
            millisecond,
            microsecond,
            nanosecond
        ).value
    }
    open val asHalfDays: Double by lazy {
        Khrono.combineAll(
            KhronoUnit.HALF_DAY,
            hour,
            minute,
            second,
            millisecond,
            microsecond,
            nanosecond
        ).value
    }
    open val asDays: Double by lazy {
        Khrono.combineAll(
            KhronoUnit.DAY,
            hour,
            minute,
            second,
            millisecond,
            microsecond,
            nanosecond
        ).value
    }
    open val asWeeks: Double by lazy {
        Khrono.combineAll(
            KhronoUnit.WEEK,
            hour,
            minute,
            second,
            millisecond,
            microsecond,
            nanosecond
        ).value
    }
    open val asYears: Double by lazy {
        Khrono.combineAll(
            KhronoUnit.YEAR,
            hour,
            minute,
            second,
            millisecond,
            microsecond,
            nanosecond
        ).value
    }
    open val asDecades: Double by lazy {
        Khrono.combineAll(
            KhronoUnit.DECADE,
            hour,
            minute,
            second,
            millisecond,
            microsecond,
            nanosecond
        ).value
    }
    open val asCenturies: Double by lazy {
        Khrono.combineAll(
            KhronoUnit.CENTURY,
            hour,
            minute,
            second,
            millisecond,
            microsecond,
            nanosecond
        ).value
    }
    open val asMillenniums: Double by lazy {
        Khrono.combineAll(
            KhronoUnit.MILLENNIUM,
            hour,
            minute,
            second,
            millisecond,
            microsecond,
            nanosecond
        ).value
    }

    fun toLocalTime(): LocalTime = LocalTime.of(
        hour.toInt(), minute.toInt(), second.toInt(),
        nanosecond.toInt() + microsecond.toNanos().toInt() + millisecond.toNanos().toInt()
    )

    fun toMutable(): MutableKhronoTime = MutableKhronoTime(
        hour.value,
        minute.value,
        second.value,
        millisecond.value,
        microsecond.value,
        nanosecond.value
    )

    fun nanosecondsSince(time: KhronoTime): Double = this.asNanos - time.asNanos
    fun microsecondsSince(time: KhronoTime): Double = this.asMicros - time.asMicros
    fun millisecondsSince(time: KhronoTime): Double = this.asMillis - time.asMillis
    fun secondsSince(time: KhronoTime): Double = this.asSeconds - time.asSeconds
    fun minutesSince(time: KhronoTime): Double = this.asMinutes - time.asMinutes
    fun hoursSince(time: KhronoTime): Double = this.asHours - time.asHours

    fun nanosecondsUntil(time: KhronoTime): Double = time.asNanos - this.asNanos
    fun microsecondsUntil(time: KhronoTime): Double = time.asMicros - this.asMicros
    fun millisecondsUntil(time: KhronoTime): Double = time.asMillis - this.asMillis
    fun secondsUntil(time: KhronoTime): Double = time.asSeconds - this.asSeconds
    fun minutesUntil(time: KhronoTime): Double = time.asMinutes - this.asMinutes
    fun hoursUntil(time: KhronoTime): Double = time.asHours - this.asHours

    override fun isBefore(upperBound: KhronoType): Boolean = this.asNanos < upperBound.asNanos
    override fun isBeforeOrAt(upperBound: KhronoType): Boolean = this.asNanos <= upperBound.asNanos

    override fun isAfter(lowerBound: KhronoType): Boolean = this.asNanos > lowerBound.asNanos
    override fun isAtOrAfter(lowerBound: KhronoType): Boolean = this.asNanos >= lowerBound.asNanos

    fun toDuration(): Duration = Duration.ofNanos(this.asNanos.toLong())
    fun toISO8601String(): String = toDuration().toString()

    override fun toString(): String =
        "${hour.toLong()}:${minute.toLong()}:${second.toLong()}.${millisecond.toLong()}.${
            microsecond.toNanos().toLong() + nanosecond.toLong()
        }"

    override fun equals(other: Any?): Boolean {
        if (other !is KhronoTime) {
            return false
        }

        if (this.nanosecond != other.nanosecond) return false
        if (this.microsecond != other.microsecond) return false
        if (this.millisecond != other.millisecond) return false
        if (this.second != other.second) return false
        if (this.minute != other.minute) return false
        if (this.hour != other.hour) return false

        return true
    }

    override fun hashCode(): Int = HashCodeBuilder.builder(this::class)
        .append(this.nanosecond)
        .append(this.microsecond)
        .append(this.millisecond)
        .append(this.second)
        .append(this.minute)
        .append(this.hour)
        .hashCode()

    override fun compareTo(other: KhronoTime): Int = when {
        this.asNanos < other.asNanos -> -1
        this.asNanos > other.asNanos -> 1
        else -> 0
    }

    // OPERATORS
    operator fun plus(other: KhronoDate) = KhronoDateTime(other, this)

    companion object {
        fun parseISO8601(iso8601Format: String): KhronoTime = Duration.parse(iso8601Format).toKhronoTime()
        fun now(): KhronoTime = LocalTime.now().toKhronoTime()

        val midnight: KhronoTime = KhronoTime(hr = 0.0)
        val afternoon: KhronoTime = KhronoTime(hr = 12.0)
    }
}