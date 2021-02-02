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

    fun toNever(): Double = KhronoUnit.NEVER_CONSTANT
    fun toNanos(): Double = unit.convertTo(value, KhronoUnit.NANOSECOND)
    fun toMicros(): Double = unit.convertTo(value, KhronoUnit.MICROSECOND)
    fun toMillis(): Double = unit.convertTo(value, KhronoUnit.MILLISECOND)
    fun toSeconds(): Double = unit.convertTo(value, KhronoUnit.SECOND)
    fun toMinutes(): Double = unit.convertTo(value, KhronoUnit.MINUTE)
    fun toHours(): Double = unit.convertTo(value, KhronoUnit.HOUR)
    fun toHalfDays(): Double = unit.convertTo(value, KhronoUnit.HALF_DAY)
    fun toDays(): Double = unit.convertTo(value, KhronoUnit.DAY)
    fun toWeeks(): Double = unit.convertTo(value, KhronoUnit.WEEK)
    fun toYears(): Double = unit.convertTo(value, KhronoUnit.YEAR)
    fun toDecades(): Double = unit.convertTo(value, KhronoUnit.DECADE)
    fun toCenturies(): Double = unit.convertTo(value, KhronoUnit.CENTURY)
    fun toMillenniums(): Double = unit.convertTo(value, KhronoUnit.MILLENNIUM)
    fun toForever(): Double = KhronoUnit.FOREVER_CONSTANT

    fun toDuration(): Duration = Duration.of(this.convertTo(KhronoUnit.NANOSECOND).toLong(), ChronoUnit.NANOS)

    fun convertTo(otherUnit: KhronoUnit) = this.unit.convertTo(this.value, otherUnit)

    fun toMutable() = MutableKhrono(value, unit)

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