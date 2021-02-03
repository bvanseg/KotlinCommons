package bvanseg.kotlincommons.time.api

/**
 * @author Boston Vanseghi
 * @since 2.8.0
 */
class MutableKhrono(value: Double, unit: KhronoUnit) : Khrono(value, unit) {

    var onChange: (() -> Unit)? = null
    var onValueChange: ((oldValue: Double, newValue: Double) -> Unit)? = null
    var onUnitChange: ((oldValue: KhronoUnit, newValue: KhronoUnit) -> Unit)? = null

    override var value: Double = value
        set(value) {
            val oldValue = field
            field = value
            onValueChange?.invoke(oldValue, value)
            onChange?.invoke()
        }

    override var unit: KhronoUnit = unit
        set(value) {
            val oldValue = field
            field = value
            onUnitChange?.invoke(oldValue, value)
            onChange?.invoke()
        }

    override fun toNever(): MutableKhrono = super.toNever().toMutable()
    override fun toNanos(): MutableKhrono = super.toNanos().toMutable()
    override fun toMicros(): MutableKhrono = super.toMicros().toMutable()
    override fun toMillis(): MutableKhrono = super.toMillis().toMutable()
    override fun toSeconds(): MutableKhrono = super.toSeconds().toMutable()
    override fun toMinutes(): MutableKhrono = super.toMinutes().toMutable()
    override fun toHours(): MutableKhrono = super.toHours().toMutable()
    override fun toHalfDays(): MutableKhrono = super.toHalfDays().toMutable()
    override fun toDays(): MutableKhrono = super.toDays().toMutable()
    override fun toWeeks(): MutableKhrono = super.toWeeks().toMutable()
    override fun toYears(): MutableKhrono = super.toYears().toMutable()
    override fun toDecades(): MutableKhrono = super.toDecades().toMutable()
    override fun toCenturies(): MutableKhrono = super.toCenturies().toMutable()
    override fun toMillenniums(): MutableKhrono = super.toMillenniums().toMutable()
    override fun toForever(): MutableKhrono = super.toForever().toMutable()

    override operator fun inc(): MutableKhrono = this.apply { this.value += 1 }
    override operator fun plus(other: Number): MutableKhrono = this.apply { this.value += other.toDouble() }
    override operator fun plus(other: Khrono): MutableKhrono = this.apply { this.value += other.convertTo(this.unit) }

    override operator fun dec(): MutableKhrono = this.apply { this.value -= 1 }
    override operator fun minus(other: Number) = this.apply { this.value -= other.toDouble() }
    override operator fun minus(other: Khrono): MutableKhrono = this.apply { this.value -= other.convertTo(this.unit) }

    companion object {
        val EMPTY = MutableKhrono(0.0, KhronoUnit.MILLISECOND)
    }
}
