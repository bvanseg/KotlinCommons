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
    override operator fun minus(other: Number): MutableKhrono = this.apply { this.value -= other.toDouble() }
    override operator fun minus(other: Khrono): MutableKhrono = this.apply { this.value -= other.convertTo(this.unit) }

    companion object {
        val NEVER: MutableKhrono = Khrono.NEVER.toMutable()
        val FOREVER: MutableKhrono = Khrono.FOREVER.toMutable()

        val EMPTY: MutableKhrono = Khrono.EMPTY.toMutable()

        fun now(): MutableKhrono = Khrono.now().toMutable()

        fun combineAll(unit: KhronoUnit, vararg times: Khrono): MutableKhrono = Khrono.combineAll(unit, *times).toMutable()

        fun parse(input: String): MutableKhrono = Khrono.parse(input).toMutable()
    }
}
