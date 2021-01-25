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
package bvanseg.kotlincommons.util.measurement.byte

/**
 *
 * @author Boston Vanseghi
 * @since 2.5.0
 */
sealed class ByteData(open var value: Long, val type: ByteUnit) {
    data class Byte(override var value: Long) : ByteData(value, ByteUnit.B)
    data class KiloByte(override var value: Long) : ByteData(value, ByteUnit.KB)
    data class MegaByte(override var value: Long) : ByteData(value, ByteUnit.MB)
    data class GigaByte(override var value: Long) : ByteData(value, ByteUnit.GB)
    data class TeraByte(override var value: Long) : ByteData(value, ByteUnit.TB)
    data class PetaByte(override var value: Long) : ByteData(value, ByteUnit.PB)

    companion object {
        fun createFromType(type: ByteUnit, value: Long) = when (type) {
            ByteUnit.B -> Byte(value)
            ByteUnit.KB -> KiloByte(value)
            ByteUnit.MB -> MegaByte(value)
            ByteUnit.GB -> GigaByte(value)
            ByteUnit.TB -> TeraByte(value)
            ByteUnit.PB -> PetaByte(value)
        }
    }
}

val Number.B: ByteData.Byte
    get() = this.bytes
val Number.bytes: ByteData.Byte
    get() = ByteData.Byte(this.toLong())

val Number.KB: ByteData.KiloByte
    get() = this.kilobytes
val Number.kilobytes: ByteData.KiloByte
    get() = ByteData.KiloByte(this.toLong())

val Number.MB: ByteData.MegaByte
    get() = this.megabytes
val Number.megabytes: ByteData.MegaByte
    get() = ByteData.MegaByte(this.toLong())

val Number.GB: ByteData.GigaByte
    get() = this.gigabytes
val Number.gigabytes: ByteData.GigaByte
    get() = ByteData.GigaByte(this.toLong())

val Number.TB: ByteData.TeraByte
    get() = this.terabytes
val Number.terabytes: ByteData.TeraByte
    get() = ByteData.TeraByte(this.toLong())

val Number.PB: ByteData.PetaByte
    get() = this.petabytes
val Number.petabytes: ByteData.PetaByte
    get() = ByteData.PetaByte(this.toLong())

val B = ByteUnit.B
val KB = ByteUnit.KB
val MB = ByteUnit.MB
val GB = ByteUnit.GB
val TB = ByteUnit.TB
val PB = ByteUnit.PB

operator fun ByteData.plus(other: ByteData): ByteData = when {
    // Ex. this.type == Kilobytes and other.type == Bytes
    this.type > other.type -> {
        val thisAmount = this into other.type
        val thatAmount = other into other.type

        val total = thisAmount + thatAmount

        ByteData.createFromType(other.type, total)
    }
    this.type < other.type || this.type == other.type -> {
        val thisAmount = this into this.type
        val thatAmount = other into this.type

        val total = thisAmount + thatAmount

        ByteData.createFromType(this.type, total)
    }
    else -> throw RuntimeException("Incomparable types: ${this.type} to ${other.type}")
}

infix fun ByteData.into(other: ByteUnit): Long {
    return this.type.to(this.value, other)
}