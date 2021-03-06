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
package bvanseg.kotlincommons.math

import bvanseg.kotlincommons.graphic.Color
import bvanseg.kotlincommons.util.project.Experimental
import java.math.BigInteger
import java.text.NumberFormat
import kotlin.math.log10

private val numberFormat = NumberFormat.getNumberInstance()

/**
 * Formats this number to a [String] with digit grouping.
 */
fun Number.format(): String = numberFormat.format(this)

/**
 * Checks whether the number is even or not.
 *
 * @return True if the [Number] is even.
 *
 * @author Boston Vanseghi
 * @since 2.0.1
 */
fun Number.isEven(): Boolean = this.toDouble() % 2 == 0.0

fun Number.toColor(): Color = Color(this.toInt())

/**
 * Checks whether the number is odd or not.
 *
 * @return True if the [Number] is odd.
 *
 * @author bright_spark
 */
fun Number.isOdd(): Boolean = !isEven()

/**
 * Checks whether the number is positive or not.
 *
 * @return True if the [Number] is positive.
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
fun Number.isPositive(): Boolean = this.toDouble() > 0

/**
 * Checks whether the number is negative or not.
 *
 * @return True if the [Number] is negative.
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
fun Number.isNegative(): Boolean = !this.isPositive()

/**
 * Returns the digit count of the given number.
 *
 * @author Boston Vanseghi
 */
@Experimental
// TODO: Testing required for float/double types
fun Number.numDigits(): Int = (log10(this.toDouble()) + 1).toInt()

@Experimental
fun Int.getDigits(): IntArray {
    if (this == 0) return intArrayOf()

    var number = this

    val array = IntArray(this.numDigits())
    var i = 0
    while (number > 0) {
        array[i++] = number % 10
        number /= 10
    }
    array.reverse()
    return array
}

/**
 * Returns the value of this [Number] as the number type [T]
 *
 * @return The value of this number as [T]
 *
 * @author bright_spark
 */
inline fun <reified T : Number> Number.to(): T = when (T::class) {
    Byte::class -> this.toByte() as T
    Short::class -> this.toShort() as T
    Int::class -> this.toInt() as T
    Long::class -> this.toLong() as T
    Float::class -> this.toFloat() as T
    Double::class -> this.toDouble() as T
    else -> throw RuntimeException("The destination type ${T::class} is not a primitive!")
}

/**
 * Will execute [block] if this number [T] is positive.
 *
 * @return This number
 *
 * @author bright_spark
 */
inline fun <reified T> T.ifPositive(block: (T) -> Unit): T where T : Number, T : Comparable<T> {
    if (this > 0.to())
        block(this)
    return this
}

/**
 * Will execute [block] if this number [T] is negative.
 *
 * @return This number
 *
 * @author bright_spark
 */
inline fun <reified T> T.ifNegative(block: (T) -> Unit): T where T : Number, T : Comparable<T> {
    if (this < 0.to())
        block(this)
    return this
}

/**
 * Returns the value of this [UByte] as a [BigInteger].
 *
 * @return The value of this number as a BigInteger
 *
 * @author bright_spark
 */
@ExperimentalUnsignedTypes
fun UByte.toBigInteger(): BigInteger = this.toLong().toBigInteger()

/**
 * Returns the value of this [UShort] as a [BigInteger].
 *
 * @return The value of this number as a BigInteger
 *
 * @author bright_spark
 */
@ExperimentalUnsignedTypes
fun UShort.toBigInteger(): BigInteger = this.toLong().toBigInteger()

/**
 * Returns the value of this [UInt] as a [BigInteger].
 *
 * @return The value of this number as a BigInteger
 *
 * @author bright_spark
 */
@ExperimentalUnsignedTypes
fun UInt.toBigInteger(): BigInteger = this.toLong().toBigInteger()

/**
 * Returns the value of this [ULong] as a [BigInteger].
 * Unlike the similar methods for smaller types, this one will use [ULong.toString] to account for values larger than a
 * [Long].
 *
 * @return The value of this number as a BigInteger
 *
 * @author bright_spark
 */
@ExperimentalUnsignedTypes
fun ULong.toBigInteger(): BigInteger = this.toString().toBigInteger()
