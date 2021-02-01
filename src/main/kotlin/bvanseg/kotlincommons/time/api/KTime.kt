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
class KTime(var value: Double, var unit: KTimeUnit) {

    init {
        when {
            value.isNaN() -> throw IllegalStateException("Expected a valid time value but got $value instead.")
            value.isInfinite() -> throw IllegalStateException("Expected a finite value but got $value instead.")
            value < 0 -> throw IllegalStateException("Time value can not be negative: $value.")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is KTime) return false
        return if (this.unit == other.unit) {
            this.unit == other.unit
        } else {
            val convertedValue = this.convertTo(other.unit)
            convertedValue == other.value
        }
    }

    override fun hashCode(): Int = HashCodeBuilder.builder(this::class).append(value).append(unit).hashCode()
    override fun toString(): String = ToStringBuilder.builder(this::class).append("value", value).append("unit", unit).toString()

    fun toNever(): Double = KTimeUnit.NEVER_CONSTANT
    fun toNanos(): Double = unit.convertTo(value, KTimeUnit.NANOSECOND)
    fun toMicros(): Double = unit.convertTo(value, KTimeUnit.MICROSECOND)
    fun toMillis(): Double = unit.convertTo(value, KTimeUnit.MILLISECOND)
    fun toSeconds(): Double = unit.convertTo(value, KTimeUnit.SECOND)
    fun toMinutes(): Double = unit.convertTo(value, KTimeUnit.MINUTE)
    fun toHours(): Double = unit.convertTo(value, KTimeUnit.HOUR)
    fun toHalfDays(): Double = unit.convertTo(value, KTimeUnit.HALF_DAY)
    fun toDays(): Double = unit.convertTo(value, KTimeUnit.DAY)
    fun toWeeks(): Double = unit.convertTo(value, KTimeUnit.WEEK)
    fun toYears(): Double = unit.convertTo(value, KTimeUnit.YEAR)
    fun toDecades(): Double = unit.convertTo(value, KTimeUnit.DECADE)
    fun toCenturies(): Double = unit.convertTo(value, KTimeUnit.CENTURY)
    fun toMillenniums(): Double = unit.convertTo(value, KTimeUnit.MILLENNIUM)
    fun toForever(): Double = KTimeUnit.FOREVER_CONSTANT

    fun toDuration(): Duration = Duration.of(this.convertTo(KTimeUnit.NANOSECOND).toLong(), ChronoUnit.NANOS)

    fun convertTo(otherUnit: KTimeUnit) = this.unit.convertTo(this.value, otherUnit)

    companion object {
        val NEVER = KTime(KTimeUnit.NEVER_CONSTANT, KTimeUnit.NEVER)
        val FOREVER = KTime(KTimeUnit.FOREVER_CONSTANT, KTimeUnit.FOREVER)

        fun now(): KTime = KTime(System.currentTimeMillis().toDouble(), KTimeUnit.MILLISECOND)
    }
}