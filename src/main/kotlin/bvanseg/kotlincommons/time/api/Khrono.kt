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
        if (other !is Khrono) return false
        return if (this.unit == other.unit) {
            this.unit == other.unit
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

    fun nextInterval(): Khrono {
        val thisAsMillis = this.toMillis().toLong()
        val snapshotMillis = System.currentTimeMillis() % thisAsMillis
        return Khrono((thisAsMillis - snapshotMillis).toDouble(), KhronoUnit.MILLISECOND)
    }

    companion object {
        val NEVER = Khrono(KhronoUnit.NEVER_CONSTANT, KhronoUnit.NEVER)
        val FOREVER = Khrono(KhronoUnit.FOREVER_CONSTANT, KhronoUnit.FOREVER)

        fun now(): Khrono = Khrono(System.currentTimeMillis().toDouble(), KhronoUnit.MILLISECOND)

        fun combineAll(unit: KhronoUnit, vararg times: Khrono): Khrono {
            var total: Double = 0.0
            times.forEach {
                total += it.convertTo(unit)
            }
            return Khrono(total, unit)
        }
    }
}