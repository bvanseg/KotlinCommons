package bvanseg.kotlincommons.time.api

import bvanseg.kotlincommons.util.project.Experimental

/**
 * Represents a measurement of time, given a value and the unit representing that value.
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
@Experimental
data class KTime(var value: Double, override var unit: KTimeUnit) : KTimeBase(unit) {

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

    fun toMillis(): Long = KTime(unit.convertTo(value, KTimeUnit.MILLISECOND), KTimeUnit.MILLISECOND).value.toLong()

    fun convertTo(otherUnit: KTimeUnit) = this.unit.convertTo(this.value, otherUnit)

    companion object {
        fun now(): KTime = KTime(System.currentTimeMillis().toDouble(), KTimeUnit.MILLISECOND)
    }
}