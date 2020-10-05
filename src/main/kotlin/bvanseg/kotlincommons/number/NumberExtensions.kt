/*
 * MIT License
 *
 * Copyright (c) 2020 Boston Vanseghi
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
package bvanseg.kotlincommons.number

import bvanseg.kotlincommons.graphic.Color
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

fun Number.numDigits(): Int = (log10(this.toDouble()) + 1).toInt()

fun Int.getDigits(): IntArray {
    if(this == 0) return intArrayOf()

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
