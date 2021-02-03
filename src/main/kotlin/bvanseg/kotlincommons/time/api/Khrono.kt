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

import bvanseg.kotlincommons.lang.string.ToStringBuilder
import bvanseg.kotlincommons.util.HashCodeBuilder
import java.time.Duration
import java.time.temporal.ChronoUnit

/**
 * Represents a measurement of time, given a value and the unit representing that value.
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
open class Khrono(open val value: Double, open val unit: KhronoUnit) {

    init {
        when {
            value.isNaN() -> throw IllegalStateException("Expected a valid time value but got $value instead.")
            value.isInfinite() -> throw IllegalStateException("Expected a finite value but got $value instead.")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Khrono) {
            return false
        }

        return if (this.unit == other.unit) {
            this.value == other.value
        } else {
            val convertedValue = this.convertTo(other.unit)
            convertedValue == other.value
        }
    }

    override fun hashCode(): Int = HashCodeBuilder.builder(this::class).append(value).append(unit).hashCode()
    override fun toString(): String =
        ToStringBuilder.builder(this::class).append("value", value).append("unit", unit).toString()

    open fun toNever(): Khrono = Khrono(KhronoUnit.NEVER_CONSTANT, KhronoUnit.NEVER)
    open fun toNanos(): Khrono = Khrono(unit.convertTo(value, KhronoUnit.NANOSECOND), KhronoUnit.NANOSECOND)
    open fun toMicros(): Khrono = Khrono(unit.convertTo(value, KhronoUnit.MICROSECOND), KhronoUnit.MICROSECOND)
    open fun toMillis(): Khrono = Khrono(unit.convertTo(value, KhronoUnit.MILLISECOND), KhronoUnit.MILLISECOND)
    open fun toSeconds(): Khrono = Khrono(unit.convertTo(value, KhronoUnit.SECOND), KhronoUnit.SECOND)
    open fun toMinutes(): Khrono = Khrono(unit.convertTo(value, KhronoUnit.MINUTE), KhronoUnit.MINUTE)
    open fun toHours(): Khrono = Khrono(unit.convertTo(value, KhronoUnit.HOUR), KhronoUnit.HOUR)
    open fun toHalfDays(): Khrono = Khrono(unit.convertTo(value, KhronoUnit.HALF_DAY), KhronoUnit.HALF_DAY)
    open fun toDays(): Khrono = Khrono(unit.convertTo(value, KhronoUnit.DAY), KhronoUnit.DAY)
    open fun toWeeks(): Khrono = Khrono(unit.convertTo(value, KhronoUnit.WEEK), KhronoUnit.WEEK)
    open fun toYears(): Khrono = Khrono(unit.convertTo(value, KhronoUnit.YEAR), KhronoUnit.YEAR)
    open fun toDecades(): Khrono = Khrono(unit.convertTo(value, KhronoUnit.DECADE), KhronoUnit.DECADE)
    open fun toCenturies(): Khrono = Khrono(unit.convertTo(value, KhronoUnit.CENTURY), KhronoUnit.CENTURY)
    open fun toMillenniums(): Khrono = Khrono(unit.convertTo(value, KhronoUnit.MILLENNIUM), KhronoUnit.MILLENNIUM)
    open fun toForever(): Khrono = Khrono(KhronoUnit.FOREVER_CONSTANT, KhronoUnit.FOREVER)

    fun toByte(): Byte = value.toInt().toByte()
    fun toShort(): Short = value.toInt().toShort()
    fun toInt(): Int = value.toInt()
    fun toLong(): Long = value.toLong()
    fun toFloat(): Float = value.toFloat()
    fun toDouble(): Double = value

    fun toDuration(): Duration = Duration.of(this.convertTo(KhronoUnit.NANOSECOND).toLong(), ChronoUnit.NANOS)

    fun convertTo(otherUnit: KhronoUnit) = this.unit.convertTo(this.value, otherUnit)

    fun toMutable() = MutableKhrono(value, unit)

    /**
     * Gets the last interval of [this] [Khrono].
     *
     * An 'interval' is determined to be the last time at which [System.currentTimeMillis] % [this] == 0L.
     * For example, consider a [Khrono] of 1 minute, which is 60,000 milliseconds. If we are 20 seconds into the current
     * minute and call this function, then it will return 20 seconds as milliseconds, since that is the time of the start
     * of the last minute interval.
     *
     * @return A [Khrono] containing the last interval time in [KhronoUnit.MILLISECOND]s.
     */
    fun lastInterval(): Khrono {
        val thisAsMillis = this.toMillis().toLong()
        val snapshotMillis = System.currentTimeMillis() % thisAsMillis
        return Khrono(snapshotMillis.toDouble(), KhronoUnit.MILLISECOND)
    }

    /**
     * Gets the next interval of [this] [Khrono].
     *
     * An 'interval' is determined to be the next time at which [System.currentTimeMillis] % [this] == 0L.
     * For example, consider a [Khrono] of 1 minute, which is 60,000 milliseconds. If we are 20 seconds into the current
     * minute and call this function, then it will return 40 seconds as milliseconds, since that is the time of the start
     * of the next minute interval.
     *
     * @return A [Khrono] containing the next interval time in [KhronoUnit.MILLISECOND]s.
     */
    fun nextInterval(): Khrono {
        val thisAsMillis = this.toMillis().toLong()
        val snapshotMillis = System.currentTimeMillis() % thisAsMillis
        return Khrono((thisAsMillis - snapshotMillis).toDouble(), KhronoUnit.MILLISECOND)
    }

    // INFIX
    infix fun into(unit: KhronoUnit): Khrono {
        val newValue = this.unit.convertTo(this.value, unit)
        return Khrono(newValue, unit)
    }

    // OPERATORS
    open operator fun inc(): Khrono = Khrono(this.value + 1, this.unit)
    open operator fun plus(other: Number): Khrono = Khrono(this.value + other.toDouble(), this.unit)
    open operator fun plus(other: Khrono): Khrono = Khrono(this.value + other.convertTo(this.unit), this.unit)

    open operator fun dec(): Khrono = Khrono(this.value - 1, this.unit)
    open operator fun minus(other: Number): Khrono = Khrono(this.value - other.toDouble(), this.unit)
    open operator fun minus(other: Khrono): Khrono = Khrono(this.value - other.convertTo(this.unit), this.unit)

    operator fun compareTo(other: Number): Int {
        val otherValue = other.toDouble()
        return when {
            this.value == otherValue -> 0
            this.value > otherValue -> 1
            this.value < otherValue -> -1
            else -> -1
        }
    }

    operator fun compareTo(other: Khrono): Int {
        val equalUnitValue = this.convertTo(other.unit)
        return when {
            equalUnitValue == other.value -> 0
            equalUnitValue > other.value -> 1
            equalUnitValue < other.value -> -1
            else -> -1
        }
    }

    open operator fun rangeTo(other: Khrono): Khrono = other - this

    companion object {
        val NEVER = Khrono(KhronoUnit.NEVER_CONSTANT, KhronoUnit.NEVER)
        val FOREVER = Khrono(KhronoUnit.FOREVER_CONSTANT, KhronoUnit.FOREVER)

        val EMPTY = Khrono(0.0, KhronoUnit.MILLISECOND)

        fun now(): Khrono = Khrono(System.currentTimeMillis().toDouble(), KhronoUnit.MILLISECOND)

        fun combineAll(unit: KhronoUnit, vararg times: Khrono): Khrono =
            Khrono(times.sumByDouble { it.convertTo(unit) }, unit)
    }
}