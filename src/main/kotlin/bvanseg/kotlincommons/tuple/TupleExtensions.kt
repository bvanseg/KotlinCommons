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
package bvanseg.kotlincommons.tuple

/**
 * Checks if the [value] is between [Pair.first] and [Pair.second] (exclusive)
 *
 * @author bright_spark
 * @since 2.0.1
 */
fun <T : Comparable<T>> Pair<T, T>.between(value: T): Boolean = value > first && value < second

/**
 * Checks if the [value] is between [Pair.first] and [Pair.second] (inclusive)
 *
 * @author bright_spark
 * @since 2.0.1
 */
fun <T : Comparable<T>> Pair<T, T>.contains(value: T): Boolean = value in first..second


/**
 * Converts this triple into a list.
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
fun <T> Triple<T, T, T>.toList(): List<T> = listOf(first, second, third)

/**
 * Converts this quad into a list.
 */
fun <T> Quad<T, T, T, T>.toList(): List<T> = listOf(first, second, third, fourth)

/**
 * Converts this triple into a list.
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
fun <T> MutableTriple<T, T, T>.toList(): List<T> = listOf(first, second, third)

/**
 * Converts this quad into a list.
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
fun <T> MutableQuad<T, T, T, T>.toList(): List<T> = listOf(first, second, third, fourth)

/**
 * Creates a tuple of type [MutablePair] from this and [that].
 *
 * @author bright_spark
 * @since 2.2.5
 */
infix fun <A, B> A.toMut(that: B): MutablePair<A, B> = MutablePair(this, that)
