package bvanseg.kotlincommons.time.api

/**
 * Represents a measurement of time, given a value and the unit representing that value.
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
data class KTime(var value: Double, override var unit: KTimeUnit): KTimeBase(unit) {

    init {
        when {
            value.isNaN() -> throw IllegalStateException("Expected a valid time value but got $value instead.")
            value.isInfinite() -> throw IllegalStateException("Expected a finite value but got $value instead.")
            value < 0 -> throw IllegalStateException("Time value can not be negative: $value.")
        }
    }

    fun toMillis(): Long = KTime(unit.convertTo(value, KTimeUnit.MILLISECOND), KTimeUnit.MILLISECOND).value.toLong()
}