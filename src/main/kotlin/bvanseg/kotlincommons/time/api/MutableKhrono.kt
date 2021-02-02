package bvanseg.kotlincommons.time.api

/**
 * @author Boston Vanseghi
 * @since 2.8.0
 */
data class MutableKhrono(override var value: Double, override var unit: KhronoUnit) : Khrono(value, unit) {
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
}
