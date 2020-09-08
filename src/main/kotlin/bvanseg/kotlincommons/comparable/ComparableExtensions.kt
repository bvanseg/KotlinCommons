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
package bvanseg.kotlincommons.comparable

/**
 * Clamps the [Comparable] object between a [min] and a [max].
 *
 * @param min The minimum that the value can not fall under.
 * @param max The maximum that the value can not go over.
 *
 * @return The [Comparable] value between the minimum or maximum.
 *
 * @author bright_spark
 * @since 1.0.1
 */
fun <T : Comparable<T>> T.clamp(min: T, max: T): T = when {
    this < min -> min
    this > max -> max
    else -> this
}

/**
 * Clamps the [Comparable] object between a [min] and a [max].
 *
 * @param min The minimum that the value can not fall under.
 * @param max The maximum that the value can not go over.
 *
 * @return The [Comparable] value between the minimum or maximum or null if the value is null.
 *
 * @author bright_spark
 * @since 1.0.1
 */
fun <T : Comparable<T>> T?.clampOrNull(min: T, max: T): T? = when {
    this == null -> null
    this < min -> min
    this > max -> max
    else -> this
}