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
package bvanseg.kotlincommons.grouping.collection.iterable

/**
 * Correlates every element in the [Iterable] to a [Byte] value and then sums all [Byte] values together.
 *
 * @author Boston Vanseghi
 */
inline fun <T> Iterable<T>.sumByByte(selector: (T) -> Byte): Byte {
    var sum = 0
    for (element in this) {
        sum += selector(element)
    }
    return sum.toByte()
}

/**
 * Correlates every element in the [Iterable] to a [Short] value and then sums all [Short] values together.
 *
 * @author Boston Vanseghi
 */
inline fun <T> Iterable<T>.sumByShort(selector: (T) -> Short): Short {
    var sum = 0L
    for (element in this) {
        sum += selector(element)
    }
    return sum.toShort()
}

/**
 * Correlates every element in the [Iterable] to a [Long] value and then sums all [Long] values together.
 *
 * @author Boston Vanseghi
 */
inline fun <T> Iterable<T>.sumByLong(selector: (T) -> Long): Long {
    var sum = 0L
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

/**
 * Correlates every element in the [Iterable] to a [Float] value and then sums all [Float] values together.
 *
 * @author Boston Vanseghi
 */
inline fun <T> Iterable<T>.sumByFloat(selector: (T) -> Float): Float {
    var sum = 0f
    for (element in this) {
        sum += selector(element)
    }
    return sum
}