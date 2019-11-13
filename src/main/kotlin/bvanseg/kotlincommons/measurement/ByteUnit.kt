/*
 * MIT License
 *
 * Copyright (c) 2019 Boston Vanseghi
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
package bvanseg.kotlincommons.measurement

/**
 * Used to represent byte data in its various scales.
 *
 * @author bright_spark
 * @since 2.0.1
 */
enum class ByteUnit {
    B,
    KB,
    MB,
    GB,
    TB;

    fun to(value: Long, unit: ByteUnit): Long {
        val diff = ordinal - unit.ordinal
        return when {
            diff == 0 -> value
            diff > 0 -> value.shl(diff * 10)
            else -> value.shr(diff * -10)
        }
    }

    fun toBytes(value: Long): Long = to(value, B)
    fun toKilobytes(value: Long): Long = to(value, KB)
    fun toMegabytes(value: Long): Long = to(value, MB)
    fun toGigabytes(value: Long): Long = to(value, GB)
    fun toTerabytes(value: Long): Long = to(value, TB)
}